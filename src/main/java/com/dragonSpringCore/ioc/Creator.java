package com.dragonSpringCore.ioc;

import com.dragonSpringCore.annotation.Component;
import com.dragonSpringCore.annotation.Controller;

import java.util.List;
import java.util.Map;

/*
* 该类根据扫描到的所有类，进行类的实例化
* 遍历类，获得其注解，根据注解进行实例化
* */
public class Creator {
    public static void doCreat(Map<String,Object> iocMap ,List<String> classNameList) {
        if (classNameList.isEmpty()) {
            return;
        }

        try {
            for (String className : classNameList) {

                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = clazz.newInstance();

                    // 将实例保存在 ioc 容器
                    iocMap.put(beanName, instance);

                } else if (clazz.isAnnotationPresent(Component.class)) {

                    //将类名的首字母转为小写，作为类在ioc容器中实例的名称
                    String beanName = toLowerFirstCase(clazz.getSimpleName());

                    // 如果注解包含自定义名称
                    Component component = clazz.getAnnotation(Component.class);
                    if (!"".equals(component.value())) {
                        beanName = component.value();
                    }

                    Object instance = clazz.newInstance();
                    iocMap.put(beanName, instance);

                    //System.out.println("[INFO-3] {" + beanName + "} has been saved in iocMap.");

                    // 找类的接口
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (iocMap.containsKey(i.getName())) {
                            throw new Exception("类名重复！");
                        }

                        iocMap.put(i.getName(), instance);
                        //System.out.println("[INFO-3] {" + i.getName() + "} has been saved in iocMap.");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String toLowerFirstCase(String className) {
        char[] charArray = className.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}
