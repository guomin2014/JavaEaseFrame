package com.gm.javaeaseframe.core.context.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.util.HttpUtil;
import com.gm.javaeaseframe.core.constains.FormState;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.Result;
import com.gm.javaeaseframe.core.context.service.ICRUDService;
import com.gm.javaeaseframe.core.context.service.IUser;

// 日志需要统一拦截
// 需要采用一种token机制，避免页面重复提交
public abstract class BaseCRUDMappingController<TS extends ICRUDService<T, PK>, TF extends BaseCRUDForm<T, PK>, T extends BaseEntity<PK>, PK extends Serializable> extends BaseController {

    /** 对应的service */
    protected TS service = null;
    /** listView视图 */
    protected String listView = "";
    /** editView视图 */
    protected String editView = "";
    /** 查看详细视图 */
    protected String viewView = "";
    /** form的class */
    protected Class<?> formClass = null;
    /** 模块描述 */
    protected String moduleDesc = "";
    
    /** 获取service对象 */
    public TS getService() {
        return service;
    }

    /** 设置service对象 */
    @Autowired
    public void setService(TS service) {
        this.service = service;
    }

    /**
     * 初始化方法
     * @param request
     * @param response
     * @param form
     */
    protected void init(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) {

    }

    /**
     * 查询处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping("list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, TF form) {

        Map<String, Object> model = new HashMap<String, Object>();
        ModelAndView modelAndView = null;
        Context context = getContext();
        String busiDesc = "查询" + getModuleDesc();
        try {
            doListBefore(request, response, form, model, context);
            Result<T> result = getService().find(form.getQuery(), form.getPageInfo(), context);
            model.put(KEY_RESULT_LIST, result.getList());
            model.put(KEY_RESULT_PAGE, result.getPageInfo());
            model.putAll(form.getModel());
            modelAndView = doListAfter(request, response, form, model, context);
            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
        }
        form.getModel().clear();
        model.put("query", form.getQuery());
        init(request, response, form, model, context);
        if (modelAndView == null) {
            modelAndView = new ModelAndView(getListView(), model);
        }
        return modelAndView;
    }

    /**
     * 查询前预处理
     * @param request
     * @param response
     * @param form
     */
    protected void doListBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * 查询后处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    protected ModelAndView doListAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {
        return null;
    }

    /**
     * 修改取对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping("edit")
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        Context context = getContext();
        String busiDesc = "获取待修改" + getModuleDesc();
        if (form.getId() == null || form.getId().length == 0) {
            form.getModel().put(KEY_RESULT_MSG, "请选择待编辑" + getModuleDesc());
            return this.list(request, response, form);
        }
        try {
            editBefore(request, response, form, model, context);
            T entity = service.get(form.getId()[0], context);
            editAfter(request, response, form, model, entity, context);
            if (entity == null) {
                throw new Exception(getModuleDesc() + "不存在或已被删除");
            }
            model.put("entity", entity);
            model.put(getModelName(form), entity);
            model.put(SysConstains.FORM_DISABLED_KEY, "");
            model.put(KEY_RESULT_PAGE, form.getPageInfo());
            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            return this.list(request, response, form);
        }
        // 保存token
        saveToken(request, response);
        init(request, response, form, model, context);
        setFormEdit(request);
        return new ModelAndView(getEditView(), model);
    }

    /**
     * 获取编辑对象前处理
     * @param request
     * @param response
     * @param form
     * @param context
     */
    protected void editBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * (可用于初始化列表等到上下文中)
     */
    protected ModelAndView editAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, T entity, Context context) throws BusinessException {
        return null;
    }

    /**
     * 新增对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping("add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        Context context = getContext();
        String busiDesc = "打开新增" + getModuleDesc() + "页面";
        try {
            addBefore(request, response, form, model);
            if (form.getEntity() != null) {
                form.getEntity().initAttrValue();
            }
            model.put("entity", form.getEntity());
            model.put(getModelName(form), form.getEntity());
            model.put(SysConstains.FORM_DISABLED_KEY, "");
            model.put(KEY_RESULT_PAGE, form.getPageInfo());
            addAfter(request, response, form, model);
            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
        }
        // 保存token信息
        saveToken(request, response);
        setFormAdd(request);
        init(request, response, form, model, context);
        return new ModelAndView(getEditView(), model);
    }

    protected void addBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model) throws BusinessException {

    }

    protected void addAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model) throws BusinessException {

    }
    @RequestMapping("delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, TF form) {
        Context context = getContext();
        if (form.getId() == null || form.getId().length == 0) {
            form.getModel().put(KEY_RESULT_MSG, "请至少选择一条记录");
        }
        String busiDesc = "删除" + getModuleDesc();
        try {
            deleteBefore(request, response, form, context);
            int iRet = service.remove(form.getId(), context);
            form.getModel().put(KEY_RESULT_MSG, getModuleDesc() + "删除成功!删除条数:" + iRet);
            recordSysLog(request, busiDesc + " 【成功】 [id:" + StringUtils.join(form.getId()) + "]");
        } catch (Exception e) {
            doException(request, busiDesc, form.getModel(), e);
        }
        return this.list(request, response, form);
    }

    protected void deleteBefore(HttpServletRequest request, HttpServletResponse response, TF form, Context context) throws BusinessException {

    }

    /**
     * 保存对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    @RequestMapping("save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        ModelAndView modelAndView = null;
        Context context = getContext();
        boolean tokenValid = true;
        FormState formState = null;
        String busiDesc = "保存" + getModuleDesc();
        try {
        	long currTime = System.currentTimeMillis();
            tokenValid = isTokenValid(request, response, true);
            if (tokenValid) {
                saveBefore(request, response, form, model, context);
                T entity = form.getEntity();
                if (entity.newEntity()) {
                	busiDesc = "新增" + getModuleDesc();
                    formState = FormState.ADD;
                    entity.setCreateTime(currTime);
                    IUser user = this.getCurUser();
                    if (user != null) {
                    	entity.setCreateUserId(user.getId());
                        entity.setCreateUserName(user.getRealName());
                    }
                    service.save(entity, context);
                } else {
                	busiDesc = "修改" + getModuleDesc();
                    formState = FormState.EDIT;
                    entity.setUpdateTime(currTime);
                    IUser user = this.getCurUser();
                    if (user != null) {
                        entity.setUpdateUserName(user.getRealName());
                    }
                    service.update(entity, context);
                }
                model.put(SysConstains.FORM_STATE, formState);
                form.setExcuteResult(VALUE_RESULT_SUCCESS);
                model.put(SysConstains.FORM_RESULT_KEY, VALUE_RESULT_SUCCESS);
                modelAndView = saveAfter(request, response, form, model, context);
                model.put(KEY_RESULT_MSG, busiDesc + "成功");
                recordSysLog(request, busiDesc + " 【成功】 [id:" + entity.getId() + "]");
            } else {
                recordSysLog(request, busiDesc + " 【失败】 重复提交");
            }
            form.setModel(model);
        } catch (Exception e) {
            form.setExcuteResult(VALUE_RESULT_FAILURE);
            model.put(SysConstains.FORM_RESULT_KEY, VALUE_RESULT_FAILURE);
            doException(request, busiDesc, model, e);
            if(formState == FormState.ADD)
            {
            	form.getEntity().resetId(null);
            }
            model.put("entity", form.getEntity());
            model.put(getModelName(form), form.getEntity());
            model.put(SysConstains.FORM_DISABLED_KEY, "");
            init(request, response, form, model, context);
            if (tokenValid) {
                saveToken(request, response);
            }
            modelAndView = saveException(request, response, form, model, context, e);
            if (modelAndView == null) {
                modelAndView = new ModelAndView(getEditView(), model);
            }
            return modelAndView;
        }
//        setFormState(request, formState);
        if (modelAndView == null) {
            modelAndView = this.list(request, response, form);
        } else {
            init(request, response, form, model, context);
        }
        return modelAndView;
    }

    /**
     * 保存前预处理
     * @param request
     * @param response
     * @param form
     */
    protected void saveBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * 保存后预处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    protected ModelAndView saveAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {
        return null;
    }

    /**
     * 保存异常拦截处理
     * @param request
     * @param response
     * @param form
     * @param model
     * @param context
     * @return
     * @throws BusinessException
     */
    protected ModelAndView saveException(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context, Exception e) {
        return null;
    }
    @RequestMapping("view")
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        if (form.getId() == null || form.getId().length == 0) {
            form.getModel().put(KEY_RESULT_MSG, "请选择待查看" + getModuleDesc() + "信息");
            return this.list(request, response, form);
        }
        String busiDesc = "查看" + getModuleDesc();
        Context context = getContext();
        try {
            viewBefore(request, response, form, model, context);
            T entity = service.get(form.getId()[0], context);
            viewAfter(request, response, form, model, entity, context);
            if (entity == null) {
                throw new Exception(busiDesc + "，不存在或已被删除");
            }
            String retuBackUrl = request.getParameter("retuBackUrl");
            if (retuBackUrl == null) {
                retuBackUrl = "";
            }
            model.putAll(form.getModel());
            model.put("retuBackUrl", retuBackUrl);
            model.put("entity", entity);
            model.put(getModelName(form), entity);
            model.put(KEY_RESULT_PAGE, form.getPageInfo());
            model.put(SysConstains.FORM_DISABLED_KEY, "disabled");
            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            form.setModel(model);
            return this.list(request, response, form);
        }
        init(request, response, form, model, context);
        setFormView(request);
        String view = getView();
        if (StringUtils.isEmpty(view)) {
            view = getEditView();
        }
        return new ModelAndView(view, model);
    }

    /**
     * 获取编辑对象前处理
     * @param request
     * @param response
     * @param form
     * @param context
     */
    protected void viewBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * (可用于初始化列表等到上下文中)
     */
    protected ModelAndView viewAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, T entity, Context context) throws BusinessException {
        return null;
    }

    protected String getModelName(TF form) {
        String entityName;
        entityName = form.getEntity().getClass().getSimpleName();
        entityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1, entityName.length());
        if (entityName.endsWith("Entity")) {
            entityName = entityName.substring(0, entityName.length() - "Entity".length());
        }
        return entityName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes"})
    protected final Object newCommandObject(Class clazz) throws Exception {
        Object obj = null;
        if (formClass != null) {// 如设置了form类类型，创建设置类实例
            obj = formClass.newInstance();
        } else if (super.isEqual(clazz.getName(), BaseCRUDForm.class.getName())) {
            // 获取当前实例的泛型的真实类类型
            Type ptype = this.getClass().getGenericSuperclass();
            if (ptype instanceof ParameterizedType) {
                // 获取本类泛型TF的运行时实际类类型
                Type type = ((ParameterizedType) ptype).getActualTypeArguments()[1];// 1代表当前类的第二个泛型参数，即TF的运行时类型
                if (type instanceof Class) {// 根据实际类型创建form实例
                    Class<TF> reqClz = (Class<TF>) type;
                    obj = reqClz.newInstance();
                }
            }
        }
//        if (obj == null) {
//            return super.newCommandObject(clazz);
//        }
        return obj;
    }

    /** 获取listView视图 */
    public String getListView() {
        return this.listView;
    }

    /** 设置listView视图 */
    public void setListView(String listView) {
        this.listView = listView;
    }

    /** 获取EditView视图 */
    public String getEditView() {
        return editView;
    }

    public String getView() {
        return viewView;
    }

    public void setView(String view) {
        this.viewView = view;
    }
    
    public void setViewView(String viewView)
	{
		this.viewView = viewView;
	}

	/** 设置editView视图 */
    public void setEditView(String editView) {
        this.editView = editView;
    }

    /**
     * 获取form class类名
     * @return
     */
    public Class<?> getFormClass() {
        return formClass;
    }

    /**
     * 设置form calss类名
     * @param formClass
     */
    public void setFormClass(Class<?> formClass) {
        this.formClass = formClass;
    }

    /**
     * 获取模块描述
     * @return
     */
    public String getModuleDesc() {
        if (StringUtils.isEmpty(moduleDesc)) {
            return "信息";
        } else {
            return moduleDesc;
        }

    }

    /**
     * 设置模块描述
     * @param moduleDesc
     */
    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    /**
     * 导出文件的方法 调用此方法，子类必须复写dodoExportBefore，注入要到处的参数
     */
    @RequestMapping("doExport")
    public void doExport(HttpServletRequest request, HttpServletResponse response, TF form) {
        OutputStream os = null;
        Context context = getContext();
        Map<String, Object> model = new HashMap<String, Object>();
        String busiDesc = "导出" + getModuleDesc();
        try {

            doExportBefore(context, form);

            // byte[] fileByte = getService().getExportByts(form.getQuery(),
            // context);
            byte[] fileByte = getService().export(form.getQuery(), context);
            if (fileByte == null) {
            	super.recordSysLog(request, busiDesc + " 【失败】 没有可导出的数据");
                this.writeResponse(response, "没有可导出的数据");
                return;
            }
            String name = StringUtils.trim(moduleDesc);
            if (StringUtils.isEmpty(name)) {
                name = System.currentTimeMillis() + "";
            }
            String fileName = name + ".xls";// (String)context.getAttribute("exportFileName");
            // FileUtil.downLoadFile(response, fileByte, fileName);

            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            HttpUtil.setCookieValue(request, response, form.getCookieName(), form.getCookieValue());
            os = response.getOutputStream();
            os.write(fileByte);
            os.flush();
            super.recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            this.writeResponse(response, this.convertException(e));
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void doExportBefore(Context context, TF form) throws BusinessException {

    }
    @RequestMapping("doDownload")
    public void doDownload(HttpServletRequest request, HttpServletResponse response, TF form) {
    	OutputStream os = null;
		InputStream inputStream = null;
        Context context = getContext();
        Map<String, Object> model = new HashMap<String, Object>();
        String busiDesc = "下载" + getModuleDesc() + "文件";
        try {

            doExportBefore(context, form);

            String fileName = request.getParameter("fileName");
			String filePath = request.getParameter("filePath");
			log.info("开始下载文件:" + fileName + "-->" + filePath);
			File file = new File(filePath);
			if(StringUtils.isEmpty(fileName))
			{
				fileName = file.getName();
			}
			inputStream = new FileInputStream(file);// 文件的存放路径
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            HttpUtil.setCookieValue(request, response, form.getCookieName(), form.getCookieValue());
            os = response.getOutputStream();
			byte[] b = new byte[1024];
	        int len;
	        while ((len = inputStream.read(b)) > 0){
	        	os.write(b, 0, len);	        	
	        }
			os.flush();
            super.recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            this.writeResponse(response, this.convertException(e));
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
            if (inputStream != null) {
            	try {
            		inputStream.close();
            	} catch (Exception e) {
            	}
            }
        }
    }
    
    public void doDownloadBefore(Context context, TF form) throws BusinessException {

    }

    protected void writeResponse(HttpServletResponse response, String msg) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(msg);
            out.flush();
        } catch (Exception e) {
        	log.debug("响应信息异常-->" + e.getMessage());
        }
    }
}
