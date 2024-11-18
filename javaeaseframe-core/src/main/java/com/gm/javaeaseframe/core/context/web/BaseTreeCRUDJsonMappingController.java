package com.gm.javaeaseframe.core.context.web;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gm.javaeaseframe.common.exception.BusinessException;
import com.gm.javaeaseframe.common.util.DataUtil;
import com.gm.javaeaseframe.core.constains.FormState;
import com.gm.javaeaseframe.core.constains.SysConstains;
import com.gm.javaeaseframe.core.context.model.BaseTreeEntity;
import com.gm.javaeaseframe.core.context.model.Context;
import com.gm.javaeaseframe.core.context.model.PageInfo;
import com.gm.javaeaseframe.core.context.model.Result;
import com.gm.javaeaseframe.core.context.model.TreeNode;
import com.gm.javaeaseframe.core.context.service.ICRUDService;

public abstract class BaseTreeCRUDJsonMappingController<TS extends ICRUDService<T, PK>, TF extends BaseCRUDForm<T, PK>, T extends BaseTreeEntity<PK>, PK extends Serializable> 
extends BaseCRUDJsonMappingController<TS, TF, T, PK> {

	@SuppressWarnings("unchecked")
	@Override
	protected int saveAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model, Context context) throws BusinessException
	{
		List<PK> list = new ArrayList<PK>();
		String state = DataUtil.conver2String(model.get(SysConstains.FORM_STATE));
		if(FormState.EDIT.toString().equals(state))
		{
			list.add(form.getEntity().getId());
		}
		else
		{
			list.add(form.getEntity().getParentId());
		}
		form.setId(list.toArray((PK[])Array.newInstance(list.get(0).getClass(), 1)));
		form.getModel().put("reloadTreeNode", true);
		model.put(KEY_RESULT_ENTITY, form.getEntity());
		return VALUE_RESULT_SUCCESS;
	}
	
	@Override
	protected void addAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model)
			throws BusinessException
	{
	    if (form.getId() != null && form.getId().length > 0) {
    		T dept = service.get(form.getId()[0], false);
    		model.put("parentNode", dept);
    		form.getEntity().resetParentId(form.getId()[0]);
	    }
	}

	@Override
	protected int editAfter(HttpServletRequest request, HttpServletResponse response, TF form,
			Map<String, Object> model, T entity, Context context) throws BusinessException
	{
		if(entity != null)
		{
			T dept = service.get(entity.getParentId(), false);
			model.put("parentNode", dept);
		}
		return VALUE_RESULT_SUCCESS;
	}
	
	public String nodeIndex(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		recordSysLog(request, "查看" + getModuleDesc() + "框架信息");
		return this.createSuccJsonResp("");
	}
	
	public void getChildNodeBefore(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		
	}
	public void getChildNodeAfter(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		
	}
	@PostMapping("child")
	public String getChildNode(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		Map<String, String> selectedIds = new HashMap<String, String>();
		Map<String, Object> model = new HashMap<String, Object>();
		List<TreeNode<T>> nodeList = new ArrayList<TreeNode<T>>();
		T entry = form.getEntity();
//		PK parentId = entry.getParentId();
		try {
			this.getChildNodeBefore(request, response, form);
			PageInfo page = new PageInfo(-1);
			List<T> areaList = new ArrayList<T>();
			if (entry.getParentId() != null) {
			    Result<T> result = getService().find(entry, page, getContext());
	            if(result != null && result.getList() != null)
	            {
	                areaList.addAll(result.getList());
	            }
	            for(T entity : areaList)
	            {
	                TreeNode<T> node = new TreeNode<T>();
	                node.setId(DataUtil.conver2String(entity.getId()));
	                node.setpId(DataUtil.conver2String(entity.getParentId()));
	                node.setName(entity.getName());
	                node.setIsParent(DataUtil.conver2Int(entity.getChildSize()) > 0);
	                node.setHasChild(DataUtil.conver2Int(entity.getChildSize()) > 0);
	                node.setRemark(entity.getRemark());
	                node.setData(entity);
	                if(entity.getLevel() < 2){
	                    node.setOpen(true);
	                }else{
	                    node.setOpen(false);
	                }
	                if(entity.getParentId() == null || DataUtil.converObj2Long(entity.getParentId()) < 1)
	                {
	                    node.setChkDisabled(true);
	                }
	                if(selectedIds.containsKey(entity.getId().toString()) || selectedIds.containsKey(entity.getParentId().toString()))
	                {
	                    node.setChecked(true);
	                }
	                nodeList.add(node);
	            }
			}
			model.put("RESULT", 0);
			model.put("list", nodeList);
			this.getChildNodeAfter(request, response, form);
		} catch (Exception e) {
			model.put("RESULT", -1);
			doException(request,"获取下级节点", form.getModel(), e);
		}
		
		String jsonStr = "";
		PK parentId = entry.getParentId();
		if(parentId == null || DataUtil.converObj2Long(parentId) < 1)
		{
			jsonStr = JSONObject.toJSONString(model);
		}
		else
		{
			jsonStr = JSONArray.toJSONString(nodeList);
		}
		return jsonStr;
	}
	
	@PostMapping("child/all")
    public String getAllChildNode(HttpServletRequest request, HttpServletResponse response, TF form)
    {
//	    Map<String, String> selectedIds = new HashMap<String, String>();
        Map<String, Object> model = new HashMap<String, Object>();
        List<TreeNode<T>> nodeList = new ArrayList<TreeNode<T>>();
        T entry = form.getEntity();
//      PK parentId = entry.getParentId();
        try {
            this.getChildNodeBefore(request, response, form);
            PageInfo page = new PageInfo(-1);
            List<T> entityList = new ArrayList<T>();
            Result<T> result = getService().find(entry, page, getContext());
            if(result != null && result.getList() != null)
            {
                entityList.addAll(result.getList());
            }
            nodeList = this.convert2TreeNode(entityList);
            model.put("RESULT", 0);
            model.put("list", nodeList);
            this.getChildNodeAfter(request, response, form);
        } catch (Exception e) {
            model.put("RESULT", -1);
            doException(request,"获取下级节点", form.getModel(), e);
        }
        
        String jsonStr = "";
        PK parentId = entry.getParentId();
        if(parentId == null || DataUtil.converObj2Long(parentId) < 1)
        {
            jsonStr = JSONObject.toJSONString(model);
        }
        else
        {
            jsonStr = JSONArray.toJSONString(nodeList);
        }
        return jsonStr;
    }
	
	public List<TreeNode<T>> convert2TreeNode(List<T> entityList) {
        List<TreeNode<T>> nodeList = new ArrayList<TreeNode<T>>();
        for(T entity : entityList)
        {
            TreeNode<T> node = new TreeNode<T>();
            node.setId(DataUtil.conver2String(entity.getId()));
            node.setpId(DataUtil.conver2String(entity.getParentId()));
            node.setName(entity.getName());
            node.setIsParent(DataUtil.conver2Int(entity.getChildSize()) > 0);
            node.setHasChild(DataUtil.conver2Int(entity.getChildSize()) > 0);
            node.setRemark(entity.getRemark());
            node.setData(entity);
            node.setLevel(entity.getLevel());
            if(entity.getLevel() < 2){
                node.setOpen(true);
            }else{
                node.setOpen(false);
            }
            if(entity.getParentId() == null || DataUtil.converObj2Long(entity.getParentId()) < 1)
            {
                node.setChkDisabled(true);
            }
            List<T> childList = (List<T>)entity.getChildList();
            List<TreeNode<T>> childNodes = this.convert2TreeNode(childList);
            node.setChildList(childNodes);
            nodeList.add(node);
        }
        return nodeList;
    }
   
}
