/**
* 文件：WeatherService.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/

package com.gm.javaeaseframe.demo.weather.service;

import java.util.List;

import com.gm.javaeaseframe.core.context.service.IApplicationService;
import com.gm.javaeaseframe.core.context.service.ICRUDService;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherService service接口 </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */

public interface WeatherService extends ICRUDService<WeatherEntity,Long>, IApplicationService{
    /**
     * 获取指定区域的今天的天气
     * 如果指定区域不存在，则获取指定区域的上级区域天气
     * @param areaId
     * @return
     */
    WeatherEntity getTodayByArea(Long areaId);
    /**
     * 获取指定区域的天气预报（最近7天）
     * 如果指定区域不存在，则获取指定区域的上级区域天气
     * @param areaId
     * @return
     */
    List<WeatherEntity> findForecastsByArea(Long areaId);
    /**
     * 获取指定区域的天气预报（指定时间区间）
     * 如果指定区域不存在，则获取指定区域的上级区域天气
     * @param areaId
     * @param startDate     开始时间，格式：yyyyMMdd
     * @param endDate       结束时间，格式：yyyyMMdd
     * @return
     */
    List<WeatherEntity> findForecastsByArea(Long areaId, String startDate, String endDate);
    /**
     * 保存或更新天气
     * @param entity
     */
    int saveOrUpdate(WeatherEntity entity);
    /**
     * 批量添加或更新天气
     * @param weatherList
     * @return
     */
    int saveOrUpdate(List<WeatherEntity> weatherList);
    
    /**
     * 测试事务(REQUIRED事务)
     */
    void save();
    /**
     * 测试事务(只读事务)
     */
    void search();
    /**
     * 测试事务(SUPPORTS事务)
     */
    void search2();
    /**
     * 测试事务(NOT_SUPPORTED事务)
     */
    void searchNonTx();
}