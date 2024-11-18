package com.gm.javaeaseframe.core.context.web;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

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

// 日志需要统一拦截
// 需要采用一种token机制，避免页面重复提交
public abstract class BaseTreeCRUDController<TS extends ICRUDService<T, PK>, TF extends BaseCRUDForm<T, PK>, T extends BaseTreeEntity<PK>, PK extends Serializable> 
extends BaseCRUDController<TS, TF, T, PK> {

	private String indexView;

	public String getIndexView()
	{
		return indexView;
	}

	public void setIndexView(String indexView)
	{
		this.indexView = indexView;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView saveAfter(HttpServletRequest request, HttpServletResponse response, TF form,
			Map<String, Object> model, Context context) throws BusinessException
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
		return super.view(request, response, form);
	}
	
	@Override
	protected void addAfter(HttpServletRequest request, HttpServletResponse response, TF form, Map<String, Object> model)
			throws BusinessException
	{
		T dept = service.get(form.getId()[0], false);
		model.put("parentNode", dept);
		form.getEntity().resetParentId(form.getId()[0]);
	}

	@Override
	protected ModelAndView editAfter(HttpServletRequest request, HttpServletResponse response, TF form,
			Map<String, Object> model, T entity, Context context) throws BusinessException
	{
		if(entity != null)
		{
			T dept = service.get(entity.getParentId(), false);
			model.put("parentNode", dept);
		}
		return null;
	}
	public ModelAndView nodeIndex(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		Map<String, Object> model = new HashMap<String, Object>();
		recordSysLog(request, "查看" + getModuleDesc() + "框架信息");
		return new ModelAndView(this.indexView, model);
	}
	
	public void getChildNodeBefore(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		
	}
	public void getChildNodeAfter(HttpServletRequest request, HttpServletResponse response, TF form)
	{
		
	}
	public void getChildNode(HttpServletRequest request, HttpServletResponse response, TF form)
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
		super.writeResponse(response, jsonStr);
	}
   
}
