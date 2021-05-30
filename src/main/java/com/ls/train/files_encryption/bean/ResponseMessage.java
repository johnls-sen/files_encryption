package com.ls.train.files_encryption.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseMessage implements Serializable {

    public static Object ok() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", 1);
        map.put("message","执行成功！");
        return map;
    }

    public static Object ok(String key,Object value) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", 1);
        map.put(key,value);
        return map;
    }

    public static Object ok(String message,Object str,String key,Object value) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", 1);
        map.put(message,str);
        map.put(key,value);
        return map;
    }

    public static Object fail() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", -1);
        map.put("message","执行失败！");
        return map;
    }

    public static Object fail(String key,Object value) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", -1);
        map.put(key,value);
        return map;
    }

    public static Object fail(String key,Object value,String name, Object obj) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("flag", -1);
        map.put(key,value);
        map.put(name,obj);
        return map;
    }

    public static Object fail(Map<String,Object> map){
        map.put("flag", -1);
        return map;
    }


}
