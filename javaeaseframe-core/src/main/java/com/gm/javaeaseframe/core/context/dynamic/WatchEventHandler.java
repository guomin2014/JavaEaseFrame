package com.gm.javaeaseframe.core.context.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 该类是是负责处理watchService事件的类。 由单个线程进行维护
 * 
 * @author GM
 * @date 2018年1月18日
 */
public class WatchEventHandler implements Runnable {
    private Log logger = LogFactory.getLog(getClass());

    /** WatchService对象引用,用于监控目录 */
    private WatchService watchService;

    /** WatchKey和Path的map用于恢复文件的绝对路径 */
    private HashMap<WatchKey, Path> keyPathMap;

    /** 处理watchService监控事件的线程的引用 */
    private Thread eventHandleThread;

    /** 是否进入事件处理线程的run方法判断标志 */
    private volatile boolean isRunning;

    private IWatchEventCallback callback;
    /**
     * 要监控的事件类型
     */
    public static final WatchEvent.Kind<?>[] EVENT_KINDS = { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY };

    /**
     * 构造器
     * 
     * @param callback 监控目录变更时的回调函数
     * 
     */
    public WatchEventHandler(IWatchEventCallback callback) {
        this.callback = callback;
        this.isRunning = false;
        this.keyPathMap = new HashMap<WatchKey, Path>();
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (Throwable e) {
            logger.error("不能获取操作系统的文件监听服务", e);
        }
    }

    /**
     * 注册文件夹
     * 
     * @param dirString
     * @return
     */
    public boolean registDir(String dirString) {
        boolean bool = true;
        Path path = Paths.get(dirString);
        if (!keyPathMap.containsValue(path)) {
            try {
                WatchKey watchKey = path.register(watchService, EVENT_KINDS);
                keyPathMap.put(watchKey, path);
                logger.info("regist watch path " + path + " successfully.");
            } catch (IOException e) {
                bool = false;
                logger.error("regist watch path " + path + " failure. maybe path do not exists, please check.", e);
            }
        }
        return bool;
    }

    /**
     * 该方法在serKeyPathMap查找dirPath对应的path是否已经被注册， 如果已经被注册，则删除该path和对应的watchKey，如果此时对应的watchService
     * 已经没有注册的路径了，则从该路径中删除该watchService。 注销文件之前，要先从总map中删除该文件
     * 
     * @param filePath 要取消注册的文件的路径
     * @return 如果目录已经注册，则取消该路径的注册，返回true。如果路径没有被注册，也会true. 换句话说目前该方法通通返回true。
     * 
     */
    public void unregistDir(String filePath) {
        Set<WatchKey> keySet = keyPathMap.keySet();
        Path path = Paths.get(filePath);
        // 找到符合条件的watchKey,取消注册
        for (WatchKey key : keySet) {
            if (keyPathMap.get(key).equals(path)) {
                try {
                    // 取消注册
                    key.cancel();
                } catch (Exception e) {
                    logger.error("cancle register failure-->" + filePath, e);
                }
                // 从map中删除key
                keyPathMap.remove(key);
                logger.info("unregist watch path " + path + " successfully.");
                return;
            }
        }

    }

    /**
     * 获取keyPathMap的大小。
     * 
     * @return 返回map的大小
     */
    public int getKeyPathMapSize() {
        return keyPathMap.size();
    }

    /**
     * 判断是否在run方法当中
     * 
     * @return 如果在，则返回true,反之false
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 实现Runnable run
     */
    public void run() {
        isRunning = true;
        logger.info("start watch event handler run.");
        while (isRunning) {
            try {
                WatchKey takeKey = null;
                try {
                    // 获取WatchKey
                    takeKey = watchService.take();
                } catch (ClosedWatchServiceException e) {
                    logger.error("tack watchKey failure. watch listen is stoped");
                    isRunning = false;
                    break;
                } catch (InterruptedException e) {
                    logger.error("tack watchKey failure.", e);
                    isRunning = false;
                    break;
                }
                if (takeKey != null) {
                    // 处理事件
                    this.doHandleEvent(takeKey);
                    takeKey.reset();// 如果不重置，WatchKey使用一次过后就不能再使用，即只能监听到一次文件变化。
                }
            } catch (Exception e) {
                logger.error("handler event failure.", e);
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        isRunning = false;
        logger.info("stop watch event handler run.");
    }

    /**
     * 处理WatchService监听到的事件的方法
     * 
     * @param takeKey
     */
    private void doHandleEvent(WatchKey takeKey) {
        // 取得WatchKey对应的Path
        Path takePath = keyPathMap.get(takeKey);
        if (takePath == null) {
            logger.info("unkown WatchKey --> " + takeKey);
            return;
        }
        String handleTakePath = StringUtils.replaceChars(takePath.toString(), '\\', '/');// 监听的目录
        List<WatchEvent<?>> eventList = takeKey.pollEvents();// 发生的事件
        takeKey.reset();
        if (eventList == null || eventList.size() == 0) {
            return;
        }
        logger.debug("tack event size:" + eventList.size());
        Map<String, String> createMap = new HashMap<>();
        Map<String, String> deleteMap = new HashMap<>();
        Map<String, String> modifyMap = new HashMap<>();
        for (WatchEvent<?> event : eventList) {
            if (event == null || event.context() == null) {
                continue;
            }
            try {
                String fileName = event.context().toString();
                // 获取发生事件的文件全路径
                String absPath = handleTakePath + File.separator + fileName;
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    logger.debug("handle event. ENTRY_CREATE-->" + event.toString() + "-->" + absPath);
                    if (!modifyMap.containsKey(fileName)) {
                        createMap.put(fileName, absPath);
                    }
                    deleteMap.remove(fileName);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    logger.debug("handle event. ENTRY_DELETE-->" + event.toString() + "-->" + absPath);
                    createMap.remove(fileName);
                    modifyMap.remove(fileName);
                    deleteMap.put(fileName, absPath);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    logger.debug("handle event. ENTRY_MODIFY-->" + event.toString() + "-->" + absPath);
                    if (!createMap.containsKey(fileName)) {
                        modifyMap.put(fileName, absPath);
                    }
                } else {
                    logger.debug("handle event-->" + event.toString() + "-->" + absPath);
                }
            } catch (Throwable ex) {
                logger.error("do handler event failure.", ex);
            }
        }
        if (this.callback != null) {
            if (!createMap.isEmpty()) {
                for (Map.Entry<String, String> entry : createMap.entrySet()) {
                    this.callback.callback(entry.getKey(), entry.getValue(), WatchEventKind.ENTRY_CREATE);
                }
            }
            if (!modifyMap.isEmpty()) {
                for (Map.Entry<String, String> entry : modifyMap.entrySet()) {
                    this.callback.callback(entry.getKey(), entry.getValue(), WatchEventKind.ENTRY_MODIFY);
                }
            }
            if (!deleteMap.isEmpty()) {
                for (Map.Entry<String, String> entry : deleteMap.entrySet()) {
                    this.callback.callback(entry.getKey(), entry.getValue(), WatchEventKind.ENTRY_DELETE);
                }
            }
        }
    }

    /**
     * 启动线程
     * 
     */
    public void start() {
        logger.info("start watch event handler...");
        eventHandleThread = new Thread(this);
        eventHandleThread.setName("EventHandleThread-" + StringUtils.trim(eventHandleThread.getName()).replaceAll("Thread-", ""));
        eventHandleThread.start();
        logger.info("start watch event handler successfully.");
    }

    /**
     * 检查线程是否存活
     * 
     * @return 存活返回true,反之false
     */
    public boolean isAlive() {
        if (eventHandleThread != null) {
            return eventHandleThread.isAlive();
        } else {
            return false;
        }
    }

    /**
     * 关闭WatchService，关闭之后，线程也会随之退出，所以没有提供线程stop方法
     * 
     * @return 关闭成功返回true，关闭失败返回false
     */
    public boolean closeWatchService() {
        boolean bool = true;
        try {
            // 关闭watchService
            watchService.close();
        } catch (Exception e) {
            bool = false;
            logger.info("Close WatchService failure-->" + e.getMessage(), e);
        }
        return bool;
    }

    public void stop() {
        logger.info("stop watch event handler...");
        this.isRunning = false;
        this.closeWatchService();
        logger.info("stop watch event handler successfully.");
    }

}
