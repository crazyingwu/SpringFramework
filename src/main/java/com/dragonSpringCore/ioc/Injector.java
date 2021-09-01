package com.dragonSpringCore.ioc;

import com.dragonSpringCore.annotation.Autowire;

import java.lang.reflect.Field;
import java.util.Map;

/*
* 依赖注入类
* 根据Autowire对变量进行依赖注入
* */
public class Injector {
    public static void doInject(Map<String,Object> iocMap){
        if (iocMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : iocMap.entrySet()) {

            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowire.class)) {
                    continue;
                }

                //System.out.println("[INFO-4] Existence XAutowired.");

                // 获取注解对应的类
                Autowire autowired = field.getAnnotation(Autowire.class);
                String beanName = autowired.value().trim();

                // 获取 Autowire 注解的值
                if ("".equals(beanName)) {
                    System.out.println("[INFO] xAutowired.value() is null");
                    beanName = field.getType().getName();

                }

                System.out.println("[INFO] bean name is "+beanName);

                // 打开变量的访问权限
                field.setAccessible(true);

                try {
                    field.set(entry.getValue(), iocMap.get(beanName));

                    System.out.println("[INFO-4] field set {" + entry.getValue() + "} - {" + iocMap.get(beanName) + "}.");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
