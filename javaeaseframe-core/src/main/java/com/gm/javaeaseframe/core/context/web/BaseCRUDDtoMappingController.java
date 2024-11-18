package com.gm.javaeaseframe.core.context.web;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gm.javaeaseframe.common.annotation.CustomApiOperation;
import com.gm.javaeaseframe.core.context.model.BaseEntity;
import com.gm.javaeaseframe.core.context.service.ICRUDService;
import com.gm.javaeaseframe.core.context.web.dto.BasePageDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseRequestDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseRequestPageDto;
import com.gm.javaeaseframe.core.context.web.dto.BaseResponseDto;
import com.gm.javaeaseframe.core.context.web.dto.CommonResult;

public abstract class BaseCRUDDtoMappingController<TS extends ICRUDService<T, PK>, TF extends BaseRequestDto, TQ extends BaseRequestPageDto, TP extends BaseResponseDto, T extends BaseEntity<PK>, PK extends Serializable> extends BaseCRUDDtoController<TS,TF,TQ,TP,T,PK> {

    /**
     * 查询列表
     * @param request
     * @param response
     * @param form
     * @return
     */
    @PostMapping("list")
    @CustomApiOperation("查询列表")
    public CommonResult<BasePageDto<TP>> list(HttpServletRequest request, HttpServletResponse response, @RequestBody TQ form) {
        return super.list(request, response, form);
    }
    /**
     * 保存对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    @PostMapping("save")
    @CustomApiOperation("保存记录")
    public CommonResult<TP> save(HttpServletRequest request, HttpServletResponse response, TF form) {
        return super.save(request, response, form);
    }
    /**
     * 查看对象
     * @param request
     * @param response
     * @param id
     * @return
     */
    @GetMapping("view/{id}")
    @CustomApiOperation("查看记录")
    public CommonResult<TP> view(HttpServletRequest request, HttpServletResponse response, @PathVariable PK id) {
        return super.view(request, response, id);
    }
    /**
     * 删除对象
     * @param request
     * @param response
     * @param form
     * @return
     */
    @PostMapping("delete/{id}")
    @CustomApiOperation("删除记录")
    public CommonResult<Integer> delete(HttpServletRequest request, HttpServletResponse response, @PathVariable PK id) {
        return super.delete(request, response, id);
    }

}
