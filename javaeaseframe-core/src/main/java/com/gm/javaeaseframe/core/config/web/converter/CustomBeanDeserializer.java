package com.gm.javaeaseframe.core.config.web.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.gm.javaeaseframe.common.util.DataUtil;
import com.gm.javaeaseframe.common.util.ReflectionUtil;
import com.gm.javaeaseframe.core.config.web.resolver.RequestBodyArgument;

public class CustomBeanDeserializer extends BeanDeserializer{

    private static final long serialVersionUID = 7239197190522126265L;

    public CustomBeanDeserializer(BeanDeserializerBase src) {
        super(src);
        super._nonStandardCreation = true;
    }
    
    @Override
    public Object deserializeFromObjectUsingNonDefault(JsonParser p, DeserializationContext ctxt) throws IOException{
        final Object bean = _valueInstantiator.createUsingDefault(ctxt);
//        p.setCurrentValue(bean);
        if (p.canReadObjectId()) {
            Object id = p.getObjectId();
            if (id != null) {
                _handleTypedObjectId(p, ctxt, bean, id);
            }
        }
        if (_injectables != null) {
            injectValues(ctxt, bean);
        }
        if (_needViewProcesing) {
            Class<?> view = ctxt.getActiveView();
            if (view != null) {
                return deserializeWithView(p, ctxt, bean, view);
            }
        }
        //TODO 填充对象的普通属性
//        if (p.hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
//            String propName = p.getCurrentName();
//            do {
//                p.nextToken();
//                this.recursiveDeserialize(p, ctxt, bean, propName, this);
//            } while ((propName = p.nextFieldName()) != null);
//        }
        //填充对象中的指定属性
        this.fillRequestBodyField(p, ctxt, bean);
        return bean;
    }
    /**
     * 递归设置对象的值(支持设置对象中的对象属性)
     * @param p
     * @param ctxt
     * @param bean
     * @param propName
     * @param deserializer
     * @return
     * @throws IOException
     */
    private SettableBeanProperty recursiveDeserialize(JsonParser p, DeserializationContext ctxt, Object bean, String propName, JsonDeserializer<?> deserializer) throws IOException {
        if (deserializer instanceof CustomBeanDeserializer) {
            String objFieldName = propName;
            String[] keyPaths = StringUtils.split(propName, "\\.");
            if (keyPaths.length >= 2) {
                objFieldName = keyPaths[0];
            }
            SettableBeanProperty prop = ((CustomBeanDeserializer) deserializer).findProperty(objFieldName);
            if (prop != null) {
                if (keyPaths.length >= 2) {
                    propName = propName.substring(objFieldName.length() + 1);
                    Object objFieldValue = ReflectionUtil.getFieldValue(bean, objFieldName);
                    if (objFieldValue == null) {//初始化
                        try {
                            objFieldValue = prop.getType().getRawClass().newInstance();
                        } catch (Exception e) {
                            wrapAndThrow(e, bean, propName, ctxt);
                        }
                        prop.set(bean, objFieldValue);
                    }
                    //递归查询下级节点
                    SettableBeanProperty childProp = this.recursiveDeserialize(p, ctxt, objFieldValue, propName, prop.getValueDeserializer());
                    if (childProp == null) {
                        try {
                            prop.deserializeSetAndReturn(p, ctxt, objFieldValue);
                        } catch (Exception e) {
                            wrapAndThrow(e, objFieldValue, propName, ctxt);
                        }
                        return prop;
                    }
                    return childProp;
                } else {//没有下级了，表示当前节点是最后一级
                    try {
                       prop.deserializeSetAndReturn(p, ctxt, bean);
                    } catch (Exception e) {
                        wrapAndThrow(e, bean, propName, ctxt);
                    }
                    return prop;
                }
            }
        } else {
            SettableBeanProperty prop = _beanProperties.find(propName);
            if (prop != null) { // normal case
                try {
                    prop.deserializeSetAndReturn(p, ctxt, bean);
                } catch (Exception e) {
                    wrapAndThrow(e, bean, propName, ctxt);
                }
                return null;
            }
            handleUnknownVanilla(p, ctxt, bean, propName);
        }
        return null;
    }
    /**
     * 将RequestBody填充到对象的指定属性中
     * @param p
     * @param ctxt
     * @param bean
     */
    private void fillRequestBodyField(JsonParser p, DeserializationContext ctxt, Object bean) {
        if (this._beanProperties != null) {
            List<SettableBeanProperty> propList = new ArrayList<>();
            this._beanProperties.forEach(prop -> {
                RequestBodyArgument annotation = prop.getAnnotation(RequestBodyArgument.class);
                if (annotation != null) {
                    try {
                        Class<? extends CustomDeserializingConverter<?>> converter = annotation.converter();
                        if (converter != null) {
                            propList.add(prop);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (propList.isEmpty()) {
                return;
            }
            String requestBody = null;
            try {
                Object inputSource = p.getInputSource();//获取输入源
                Object inObj = ReflectionUtil.getFieldValue(inputSource, "in");//获取输入源的输入流
                if (inObj != null) {
                    try {
                        Object parent = ReflectionUtil.getFieldValue(inObj, "this$0");// 获取输入流对像的父级对象
                        if (parent instanceof ContentCachingRequestWrapper) {
                            ContentCachingRequestWrapper request = (ContentCachingRequestWrapper)parent;
                            requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
                        }
                    } catch (Exception ex) {
                        Object ib = ReflectionUtil.getFieldValue(inObj, "ib");
                        if (ib != null) {
                            Object bb = ReflectionUtil.getFieldValue(ib, "bb");
                            if (bb instanceof java.nio.ByteBuffer) {
                                Object coyoteRequest = ReflectionUtil.getFieldValue(ib, "coyoteRequest");
                                Object contentLength = ReflectionUtil.getFieldValue(coyoteRequest, "contentLength");
                                java.nio.ByteBuffer buffer = (java.nio.ByteBuffer)bb;
                                buffer.flip();
                                if (contentLength != null) {
                                    int len = DataUtil.conver2Int(contentLength);
                                    byte[] bys = new byte[len];
                                    buffer.position(buffer.limit()-len);
                                    buffer.get(bys, 0, len);
                                    requestBody = new String(bys);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(requestBody)) {
                return;
            }
            for (SettableBeanProperty prop : propList) {
                RequestBodyArgument annotation = prop.getAnnotation(RequestBodyArgument.class);
                if (annotation != null) {
                    try {
                        Class<? extends CustomDeserializingConverter<?>> converter = annotation.converter();
                        if (converter != null) {
                            Object value = converter.newInstance().convert(requestBody);
                            prop.set(bean, value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
