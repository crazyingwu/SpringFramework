package com.dragonSpringCore.ioc;

import com.dragonSpringCore.mvc.controller.Handler;
import com.dragonSpringCore.mvc.controller.HandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/*
IOC容器的上下文，各种map和映射map保存在context中
在Context初始化阶段进行扫描类，初始化IOC容器
 */
public class MyContext {
    public Map<String,Object> ioc;
    public List<Handler> handleMap;
    private List<String> classNameList;
    public Map<Handler, HandlerAdapter> adapterMapping;
    public MyContext(String packageName){
        ioc = new ConcurrentHashMap<String, Object>();
        handleMap = new ArrayList<>();
        classNameList = new ArrayList<>();
        adapterMapping = new ConcurrentHashMap<Handler, HandlerAdapter>();
        Scanner.doScan(packageName,classNameList);
        Creator.doCreat(ioc,classNameList);
        Injector.doInject(ioc);
    }

}
