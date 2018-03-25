package com.idreamshen.practice.spring.bean;

import com.idreamshen.practice.spring.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BeanFactory {

    private ClassLoader cl = Thread.currentThread().getContextClassLoader();
    private Class mainClazz;
    private List<Class> beanClasses = new ArrayList<>();

    public BeanFactory(Class mainClazz) {
        this.mainClazz = mainClazz;
    }

    public void initBeans() {

        String basePackage = mainClazz.getPackage().getName();
        URL url = cl.getResource(basePackage.replaceAll("\\.", "/"));

        if (url == null) throw new RuntimeException(String.format("无法定位 class 文件，package=%s", basePackage));

        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            scanAndAndClassByFile(new File(url.getPath()));
        } else if ("jar".equals(protocol)) {
            try {
                scanAndAddClassByJar(((JarURLConnection) url.openConnection()).getJarFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException(String.format("未知的 protocol=%s", protocol));
        }

        System.out.println(beanClasses.size());

    }

    private void scanAndAndClassByFile(File file) {

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                scanAndAndClassByFile(f);
            }
        } else {

            if (file.getName().endsWith(".class")) {
                String filePath = file.getAbsolutePath();
                String tmp = filePath.split("classes")[1].substring(1);
                tmp = tmp.substring(0, tmp.length() - 6);
                String className = tmp.replaceAll("/", ".");
                addClassIfIsComponent(className);
            }

        }

    }

    private void scanAndAddClassByJar(JarFile jarFile) {

        Enumeration<JarEntry> jarEntries = jarFile.entries();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.getName().endsWith(".class")) {
                String tmp = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                String className = tmp.replaceAll("/", ".");
                addClassIfIsComponent(className);
            }
        }

    }

    private boolean isComponentClass(Class clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    private void addClassIfIsComponent(String className) {
        try {
            Class clazz = cl.loadClass(className);
            if (isComponentClass(clazz)) {
                beanClasses.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
