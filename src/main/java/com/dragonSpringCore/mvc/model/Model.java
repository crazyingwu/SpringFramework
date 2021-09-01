package com.dragonSpringCore.mvc.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
* 请求中数据的传递，可以避免控制类函数与request的直接接触，使数据的输出更方便
* */
public class Model {
    private Map<String,Object> map;

    public Model(){
        map=new ConcurrentHashMap<>();
    }

    public void addAttribute(String key,Object value ){
        map.put(key,value);
    }

    public Map<String,Object> getMap(){
        return map;
    }
}
