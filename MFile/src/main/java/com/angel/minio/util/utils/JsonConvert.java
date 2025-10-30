package com.angel.minio.util.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

/**
 * @Author yangb
 * @Date 2022/4/28 15:04
 * @Version 1.0
 */
public class JsonConvert {

    private JsonConvert(){}

    /**
     * 枚举转为json
     * @param anEnum
     * @return
     */
    public static JSONObject eumToJson(Enum anEnum){
        JSONObject aliJson = new JSONObject();
        if(anEnum == null) {
            return null;
        }
        BeanWrapper src = new BeanWrapperImpl(anEnum);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String key = pd.getName();
            if("class".equals(key) || "declaringClass".equals(key)){
                continue;
            }
            aliJson.put(key,src.getPropertyValue(key));
        }
        return aliJson;
    }



}
