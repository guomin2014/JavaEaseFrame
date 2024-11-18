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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.exception.ExceptionCodeEnum;
import com.gm.javaeaseframe.common.util.ExcelExportData;
import com.gm.javaeaseframe.common.util.excel.ExcelUtils;
import com.gm.javaeaseframe.common.util.excel.IDataWriterCallback;
import com.gm.javaeaseframe.common.util.excel.common.ExcelWriterMeta;
import com.gm.javaeaseframe.core.constains.FormState;
import com.gm.javaeaseframe.core.constains.GlobalSysInfo;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.Result;
import com.gm.javaeaseframe.core.context.service.ICRUDService;
import com.gm.javaeaseframe.core.context.service.IUser;
import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseRequestDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseRequestPageDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseResponseDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;
import com.gm.javaeaseframe.core.context.web.dto.PageDto;

public abstract class BaseCRUDDtoController<TS extends ICRUDService<T, PK>, TF extends BaseRequestDto, TQ extends BaseRequestPageDto, TP extends BaseResponseDto, T extends BaseEntity<PK>, PK extends Serializable> extends BaseController {

	/** 对应的service */
    protected TS service = null;
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
     * 将DTO对象转换成model中的实例对象
     * @param dto
     * @return
     */
    public T convertDto2Entity(TF dto) {
    	T t = this.newInstanceForT();
    	BeanUtils.copyProperties(dto, t);
		return t;
    }
    /**
     * 将DTO对象转换成model中的实例对象
     * @param dto
     * @return
     */
    public T convertDto2Entity(TQ dto) {
    	T t = this.newInstanceForT();
    	BeanUtils.copyProperties(dto, t);
		return t;
    }
    /**
     * 将model中的实例对象转换成DTO对象
     * @param entity
     * @return
     */
    public TP convertEntity2Dto(T entity) {
    	TP tp = this.newInstanceForTP();
    	BeanUtils.copyProperties(entity, tp);
		return tp;
    	
    }
    public List<TP> convertEntity2Dto(List<T> list) {
    	if (list == null | list.isEmpty()) {
    		return new ArrayList<>();
    	}
    	List<TP> dtos = new ArrayList<>();
        for (T entity : list) {
            TP dto = this.newInstanceForTP();
            BeanUtils.copyProperties(entity, dto);
            dtos.add(dto);
        }
        return dtos;
    }
    /**
     * 将DTO对象转换成分页对象
     * @param dto
     * @return
     */
    public PageInfo convertDto2Page(TQ dto) {
    	PageInfo page = new PageInfo(dto.getPageSize());
		page.setCurrPage(dto.getCurrentPage());
		return page;
    }
    /**
     * 将后台分页对象转换成DTO分页
     * @param page
     * @return
     */
    public PageDto convertPage2Dto(PageInfo page) {
    	PageDto pd = new PageDto(page.getPrePageResult(), page.getCurrPage(), page.getTotalResult());
    	return pd;
    }
    /**
     * 将实体分页结果转换成通用CommonResult分页结果
     * @param result
     * @return
     */
    public CommonResult<BasePageDto<TP>> convertResult2CommonResult(Result<T> result) {
    	BasePageDto<TP> basePageDto = new BasePageDto<>();
    	if (result == null) {
    		basePageDto.setPage(new PageDto(10, 1, 0));
    	} else {
	    	PageDto page = this.convertPage2Dto(result.getPageInfo());
	    	basePageDto.setPage(page);
	    	basePageDto.setRecords(this.convertEntity2Dto(result.getList()));
    	}
    	return new CommonResult<>(basePageDto);
    }
    
    protected T newInstanceForT() {
    	// 获取当前实例的泛型的真实类类型
        Type ptype = this.getClass().getGenericSuperclass();
        if (ptype instanceof ParameterizedType) {
            // 获取本类泛型TF的运行时实际类类型
            Type type = ((ParameterizedType) ptype).getActualTypeArguments()[4];// 1代表当前类的第二个泛型参数，即TF的运行时类型
            if (type instanceof Class) {// 根据实际类型创建form实例
                Class<T> reqClz = (Class<T>) type;
                try {
					return reqClz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new BusinessException(ExceptionCodeEnum.SYSTEM_ERROR.getCode(), "不能初始化对象实例");
				}
            }
        }
        return null;
    }
    protected TP newInstanceForTP() {
    	// 获取当前实例的泛型的真实类类型
    	Type ptype = this.getClass().getGenericSuperclass();
    	if (ptype instanceof ParameterizedType) {
    		// 获取本类泛型TF的运行时实际类类型
    		Type type = ((ParameterizedType) ptype).getActualTypeArguments()[3];// 1代表当前类的第二个泛型参数，即TF的运行时类型
    		if (type instanceof Class) {// 根据实际类型创建form实例
    			Class<TP> reqClz = (Class<TP>) type;
    			try {
					return reqClz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new BusinessException(ExceptionCodeEnum.SYSTEM_ERROR.getCode(), "不能初始化对象实例");
				}
    		}
    	}
    	return null;
    }
    
    /**
     * 初始化方法
     * @param request
     * @param response
     * @param form
     */
    protected void init(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) {

    }
    protected void initForList(HttpServletRequest request, HttpServletResponse response, TQ form, T entity, Result<T> result, Map<String, Object> model, Context context) {
    	
    }

    /**
     * 查询处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    public CommonResult<BasePageDto<TP>> list(HttpServletRequest request, HttpServletResponse response, @RequestBody TQ form) {
        Map<String, Object> model = new HashMap<String, Object>();
        Context context = getContext();
        String busiDesc = "查询" + getModuleDesc();
        int code = VALUE_RESULT_SUCCESS;
        super.setFormList(request);
        CommonResult<BasePageDto<TP>> ret = new CommonResult<BasePageDto<TP>>();
        BasePageDto<TP> data = new BasePageDto<TP>();
        T entity = null;
        Result<T> result = null;
        try {
        	entity = this.convertDto2Entity(form);
            doListBefore(request, response, form, entity, model, context);
            result = getService().find(entity, this.convertDto2Page(form), context);
            model.put(KEY_RESULT_LIST, result.getList());
            model.put(KEY_RESULT_PAGE, result.getPageInfo());
            List<TP> records = new ArrayList<>();
            for (T t : result.getList()) {
            	records.add(this.convertEntity2Dto(t));
            }
            data.setRecords(records);
            data.setPage(this.convertPage2Dto(result.getPageInfo()));
            code = doListAfter(request, response, form, entity, result, model, context);
//            remove by gm 20231016 统一使用拦截器处理
//            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
        	code = VALUE_RESULT_FAILURE;
            doException(request, busiDesc, model, e);
        }
        initForList(request, response, form, entity, result, model, context);
        Object msg = model.remove(KEY_RESULT_MSG);
        ret.setCode(code);
        ret.setMsg(msg != null ? msg.toString() : null);
        ret.setData(data);
        return ret;
    }

    /**
     * 查询前预处理
     * @param request
     * @param response
     * @param form
     */
    protected void doListBefore(HttpServletRequest request, HttpServletResponse response, TQ form, T entity, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * 查询后处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    protected int doListAfter(HttpServletRequest request, HttpServletResponse response, TQ form, T entity, Result<T> result, Map<String, Object> model, Context context) throws BusinessException {
        return VALUE_RESULT_SUCCESS;
    }

    /**
     * 修改取对象
     * @param request
     * @param response
     * @param id
     * @return
     */
    public CommonResult<TP> edit(HttpServletRequest request, HttpServletResponse response, @PathVariable PK id) {
        Map<String, Object> model = new HashMap<String, Object>();
        int code = VALUE_RESULT_SUCCESS;
        Context context = getContext();
        String busiDesc = "获取待修改" + getModuleDesc();
        CommonResult<TP> ret = new CommonResult<TP>();
        if (id == null) {
        	ret.setCode(VALUE_RESULT_FAILURE);
        	ret.setMsg("请选择待编辑" + getModuleDesc());
            return ret;
        }
        setFormEdit(request);
        T entity = null;
        try {
            editBefore(request, response, id, model, context);
            entity = service.get(id, context);
            code = editAfter(request, response, id, entity, model, context);
            if (entity == null) {
                throw new Exception(getModuleDesc() + "不存在或已被删除");
            }
            model.put(KEY_RESULT_ENTITY, entity);
            ret.setData(this.convertEntity2Dto(entity));
            //remove by gm 20231016 统一使用拦截器处理
//            recordSysLog(request, busiDesc + " 【成功】");
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            ret.setCode(VALUE_RESULT_FAILURE);
        	ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
            return ret;
        }
        // 保存token
        saveToken(request, response);
        init(request, response, null, entity, model, context);
        ret.setCode(code);
        ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
        return ret;
    }

    /**
     * 获取编辑对象前处理
     * @param request
     * @param response
     * @param form
     * @param context
     */
    protected void editBefore(HttpServletRequest request, HttpServletResponse response, PK id, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * (可用于初始化列表等到上下文中)
     */
    protected int editAfter(HttpServletRequest request, HttpServletResponse response, PK id, T entity, Map<String, Object> model, Context context) throws BusinessException {
        return VALUE_RESULT_SUCCESS;
    }

    /**
     * 新增对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    public CommonResult<TP> add(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        int code = VALUE_RESULT_SUCCESS;
        Context context = getContext();
        CommonResult<TP> ret = new CommonResult<TP>();
        String busiDesc = "打开新增" + getModuleDesc() + "页面";
        setFormAdd(request);
        T entity = null;
        try {
        	entity = this.convertDto2Entity(form);
            addBefore(request, response, form, entity, model, context);
            addAfter(request, response, form, entity, model, context);
//            remove by gm 20231016 统一使用拦截器处理
//            recordSysLog(request, busiDesc + " 【成功】");
            code = VALUE_RESULT_SUCCESS;
            
        } catch (Exception e) {
        	code = VALUE_RESULT_FAILURE;
            doException(request, busiDesc, model, e);
        }
        // 保存token信息
        saveToken(request, response);
        init(request, response, form, entity, model, context);
        ret.setCode(code);
        ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
        return ret;
    }

    protected void addBefore(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) throws BusinessException {

    }

    protected void addAfter(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) throws BusinessException {

    }
    public CommonResult<Integer> delete(HttpServletRequest request, HttpServletResponse response, @PathVariable PK id) {
        Context context = getContext();
        CommonResult<Integer> ret = new CommonResult<Integer>();
        if (id == null) {
        	ret.setCode(VALUE_RESULT_FAILURE);
        	ret.setMsg("请至少选择一条记录");
            return ret;
        }
        Map<String, Object> model = new HashMap<String, Object>();
        int code = VALUE_RESULT_SUCCESS;
        String busiDesc = "删除" + getModuleDesc();
        super.setFormDelete(request);
        try {
            deleteBefore(request, response, id, model, context);
            int iRet = service.remove(id, context);
            deleteAfter(request, response, id, iRet, model, context);
            model.put(SysConstains.FORM_STATE, FormState.DELETE);
            model.put(KEY_RESULT_MSG, getModuleDesc() + "删除成功!删除条数:" + iRet);
//            recordSysLog(request, busiDesc + " 【成功】 [id:" + id + "]");remove by gm 20231016 统一使用拦截器处理
            code = VALUE_RESULT_SUCCESS;
            ret.setData(iRet);
        } catch (Exception e) {
        	code = VALUE_RESULT_FAILURE;
            doException(request, busiDesc, model, e);
        }
        ret.setCode(code);
        ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
        return ret;
    }

    protected void deleteBefore(HttpServletRequest request, HttpServletResponse response, PK id, Map<String, Object> model,Context context) throws BusinessException {

    }
    protected void deleteAfter(HttpServletRequest request, HttpServletResponse response, PK id, int result, Map<String, Object> model, Context context) throws BusinessException {
    	
    }

    /**
     * 保存对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    public CommonResult<TP> save(HttpServletRequest request, HttpServletResponse response, TF form) {
        Map<String, Object> model = new HashMap<String, Object>();
        Context context = getContext();
        boolean tokenValid = true;
        FormState formState = null;
        int code = VALUE_RESULT_SUCCESS;
        String busiDesc = "保存" + getModuleDesc();
        CommonResult<TP> ret = new CommonResult<TP>();
        T entity = null;
        try {
            //去掉重复提交校验
            tokenValid = true;//isTokenValid(request, response, true);
            if (tokenValid) {
            	long currTime = System.currentTimeMillis();
                entity = this.convertDto2Entity(form);
                if (entity.newEntity()) {
                    super.setFormAdd(request);
                } else {
                    super.setFormEdit(request);
                }
                saveBefore(request, response, form, entity, model, context);
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
                        entity.setUpdateUserId(user.getId());
                        entity.setUpdateUserName(user.getRealName());
                    }
                    service.update(entity, context);
                }
                model.put(SysConstains.FORM_STATE, formState);
                super.setFormState(request, formState);
                code = saveAfter(request, response, form, entity, model, context);
                model.put(KEY_RESULT_MSG, busiDesc + "成功");
                model.put("id", entity.getId());
                ret.setData(this.convertEntity2Dto(entity));
//                recordSysLog(request, busiDesc + " 【成功】 [id:" + entity.getId() + "]");remove by gm 20231016 统一使用拦截器处理
            } else {
//                recordSysLog(request, busiDesc + " 【失败】 重复提交");remove by gm 20231016 统一使用拦截器处理
                code = VALUE_RESULT_FAILURE;
                model.put(KEY_RESULT_MSG, "重复提交");
            }
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            if (tokenValid) {
                saveToken(request, response);
            }
            code = saveException(request, response, form, entity, model, context, e);
        }
        init(request, response, form, entity, model, context);
        ret.setCode(code);
        ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
        return ret;
    }

    /**
     * 保存前预处理
     * @param request
     * @param response
     * @param form
     */
    protected void saveBefore(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * 保存后预处理
     * @param request
     * @param response
     * @param form
     * @return
     */
    protected int saveAfter(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) throws BusinessException {
        return VALUE_RESULT_SUCCESS;
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
    protected int saveException(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context, Exception e) {
        return VALUE_RESULT_FAILURE;
    }
    public CommonResult<TP> view(HttpServletRequest request, HttpServletResponse response, @PathVariable PK id) {
        Map<String, Object> model = new HashMap<String, Object>();
        setFormView(request);
        int code = VALUE_RESULT_SUCCESS;
        String busiDesc = "查看" + getModuleDesc();
        Context context = getContext();
        CommonResult<TP> ret = new CommonResult<TP>();
        if (id == null) {
        	ret.setCode(VALUE_RESULT_FAILURE);
        	ret.setMsg("请选择待查看" + getModuleDesc() + "信息");
            return ret;
        }
        T entity = null;
        try {
            viewBefore(request, response, id, model, context);
            entity = service.get(id, context);
            viewAfter(request, response, id, entity, model, context);
            if (entity == null) {
                throw new Exception(busiDesc + "，不存在或已被删除");
            }
            model.put(KEY_RESULT_ENTITY, entity);
            ret.setData(this.convertEntity2Dto(entity));
//            recordSysLog(request, busiDesc + " 【成功】");remove by gm 20231016 统一使用拦截器处理
        } catch (Exception e) {
            doException(request, busiDesc, model, e);
            Object msg = model.get(KEY_RESULT_MSG);
            code = VALUE_RESULT_FAILURE;
            model.put(KEY_RESULT_MSG, msg == null ? "系统异常" : msg.toString());
        }
        init(request, response, null, entity, model, context);
        ret.setCode(code);
        ret.setMsg(String.valueOf(model.remove(KEY_RESULT_MSG)));
        return ret;
    }

    /**
     * 获取编辑对象前处理
     * @param request
     * @param response
     * @param form
     * @param context
     */
    protected void viewBefore(HttpServletRequest request, HttpServletResponse response, PK id, Map<String, Object> model, Context context) throws BusinessException {

    }

    /**
     * (可用于初始化列表等到上下文中)
     */
    protected int viewAfter(HttpServletRequest request, HttpServletResponse response, PK id, T entity, Map<String, Object> model, Context context) throws BusinessException {
        return VALUE_RESULT_SUCCESS;
    }


    /**
     * 获取模块描述
     * @return
     */
    public String getModuleDesc() {
        if (StringUtils.isBlank(moduleDesc)) {
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
     * 导出文件的方法 调用此方法，子类必须复写doExportBefore，注入要到处的参数
     */
    public void doExport(HttpServletRequest request, HttpServletResponse response, TF form) {
        OutputStream os = null;
        InputStream inputStream = null;
        Context context = getContext();
        Map<String, Object> model = new HashMap<String, Object>();
        String busiDesc = "导出" + getModuleDesc();
        super.setFormExport(request);
        try {
        	T entity = this.convertDto2Entity(form);
            doExportBefore(request, response, form, entity, model, context);
            
            String name = StringUtils.trim(moduleDesc);
            if (StringUtils.isBlank(name)) {
                name = System.currentTimeMillis() + "";
            }
            String fileName = name + ".xlsx";// (String)context.getAttribute("exportFileName");
            String fileRootPath = StringUtils.appendIfMissing(GlobalSysInfo.realRootPath, "/") + "file/temp/";
            File uploadRootFile = new File(fileRootPath);
            if(!uploadRootFile.exists()){
                uploadRootFile.mkdirs();
            }
            String filePath = fileRootPath + fileName;
            //分页导出
            ExcelWriterMeta meta = new ExcelWriterMeta();
            ExcelExportData data = doExportConvert(context, form, entity, null);
            if (data == null) {
//                super.recordSysLog(request, busiDesc + " 【失败】 未设置表格参数");remove by gm 20231016 统一使用拦截器处理
                this.writeResponse(response, "导出数据异常，未设置表格参数！");
                return;
            }
            if (data.getColumnName() == null || data.getColumnName().length == 0) {
//                super.recordSysLog(request, busiDesc + " 【失败】 未设置表格标题栏参数");remove by gm 20231016 统一使用拦截器处理
                this.writeResponse(response, "导出数据异常，未设置表格标题栏参数！");
                return;
            }
            String sheetName = data.getSheetName();
            if (StringUtils.isBlank(sheetName)) {
                sheetName = data.getTitle();
            }
            if (StringUtils.isBlank(sheetName)) {
                sheetName = name;
            }
            meta.setTitle(data.getTitle());
            meta.setSheetName(sheetName);
            meta.setColumnName(data.getColumnName());
            meta.setFilePath(filePath);
            ExcelUtils.writerExcel(meta, new IDataWriterCallback() {
                @Override
                public List<String[]> doCallback(int index) {
                    PageInfo pageInfo = new PageInfo(1000);
                    pageInfo.setCurrPage(index);
                    Result<T> result = getService().find(entity, pageInfo, context);
                    List<T> list = result.getList();
                    ExcelExportData data = doExportConvert(context, form, entity, list);
                    return data != null ? data.getDataList() : null;
                }
            });
            inputStream = new FileInputStream(filePath);// 文件的存放路径
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
//            HttpUtil.setCookieValue(response, form.getCookieName(), form.getCookieValue(), request.getServerName(), 120, false);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0){
                os.write(b, 0, len);                
            }
            os.flush();
//            super.recordSysLog(request, busiDesc + " 【成功】");remove by gm 20231016 统一使用拦截器处理
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

    public void doExportBefore(HttpServletRequest request, HttpServletResponse response, TF form, T entity, Map<String, Object> model, Context context) throws BusinessException {

    }
    /**
     * 导出数据参数设置方法
     * 设置导出Excel的标题、表头、数据列等信息
     * @param context
     * @param form
     * @param list
     * @return
     * @throws BusinessException
     */
    public ExcelExportData doExportConvert(Context context, TF form, T entity, List<T> list) throws BusinessException {
    	return null;
    }
    public void doDownload(HttpServletRequest request, HttpServletResponse response, TF form) {
    	OutputStream os = null;
		InputStream inputStream = null;
        Context context = getContext();
        Map<String, Object> model = new HashMap<String, Object>();
        String busiDesc = "下载" + getModuleDesc() + "文件";
        try {

        	doDownloadBefore(request, response, form, model, context);

            String fileName = request.getParameter("fileName");
			String filePath = request.getParameter("filePath");
			log.info("开始下载文件:" + fileName + "-->" + filePath);
			File file = new File(filePath);
			if(StringUtils.isBlank(fileName))
			{
				fileName = file.getName();
			}
			inputStream = new FileInputStream(file);// 文件的存放路径
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
//            HttpUtil.setCookieValue(request, response, form.getCookieName(), form.getCookieValue());
            os = response.getOutputStream();
			byte[] b = new byte[1024];
	        int len;
	        while ((len = inputStream.read(b)) > 0){
	        	os.write(b, 0, len);	        	
	        }
			os.flush();
//            super.recordSysLog(request, busiDesc + " 【成功】");remove by gm 20231016 统一使用拦截器处理
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
    
    public void doDownloadBefore(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException {

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
