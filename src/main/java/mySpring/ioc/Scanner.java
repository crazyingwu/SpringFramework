package mySpring.ioc;

import java.io.File;
import java.net.URL;
import java.util.List;

/*
* 扫描指定的包下面的所有类
* 保存到list中，准备初始化ioc容器
* */
public class Scanner {

    //扫描指令包下面的所有class，保存到list
    public static void doScan(String scanPackage, List<String> classNameList) {

        // package's . ==> /
        URL resourcePath = Scanner.class.getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        if (resourcePath == null) {
            return;
        }

        File classPath = new File(resourcePath.getFile());

        for (File file : classPath.listFiles()) {

            if (file.isDirectory()) {

                //System.out.println("[INFO-2] {" + file.getName() + "} is a directory.");

                // 子目录递归
                doScan(scanPackage + "." + file.getName(),classNameList);
            } else {

                if (!file.getName().endsWith(".class")) {
                   // System.out.println("[INFO-2] {" + file.getName() + "} is not a class file.");
                    continue;
                }

                String className = (scanPackage + "." + file.getName()).replace(".class", "");

                // 保存在list中
                classNameList.add(className);
                //System.out.println("[INFO-2] {" + className + "} has been saved in classNameList.");
            }
        }
    }
}
