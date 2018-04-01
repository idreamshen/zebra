package com.idreamshen.zebra.bean;

import com.idreamshen.zebra.annotation.Autowired;
import com.idreamshen.zebra.annotation.Component;
import com.idreamshen.zebra.annotation.Controller;
import com.idreamshen.zebra.annotation.RequestMapping;
import com.idreamshen.zebra.enums.RequestMethod;
import com.idreamshen.zebra.util.ReflectUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BeanFactory {

    private ClassLoader cl = Thread.currentThread().getContextClassLoader();
    private Class mainClazz;
    private Queue<Class> beanClasses = new LinkedList<>();
    public static Map<String, Object> beans = new HashMap<>();
    private Map<String, String> routers = new HashMap<>();

    public BeanFactory(Class mainClazz) {
        this.mainClazz = mainClazz;
    }

    public <T> T get(Class<T> clazz) {
       return (T)beans.get(clazz.getSimpleName());
    }

    public Map<String, String> getRouters() {
        return routers;
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

        generateBeans();

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

    private void addClassIfIsComponent(String className) {
        try {
            Class clazz = cl.loadClass(className);
            if (!clazz.isAnnotation() &&
                    (clazz.isAnnotationPresent(Component.class)
                    || clazz.isAnnotationPresent(Controller.class) )) {
                beanClasses.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateBeans() {

        while (!beanClasses.isEmpty()) {
            Class clazz = beanClasses.poll();
            String beanName = clazz.getSimpleName().endsWith("Impl")
                    ? clazz.getSimpleName().replace("Impl", "")
                    : clazz.getSimpleName();

            if (!beans.containsKey(beanName)) {
                beans.put(beanName, ReflectUtil.newInstance(clazz));
            }

            Object instance = beans.get(beanName);

            Field[] fields = instance.getClass().getDeclaredFields();

            boolean isUninitializedFieldExist = false;

            for (Field field : fields) {

                if (field.isAnnotationPresent(Autowired.class)) {

                    if (ReflectUtil.getFieldValue(field, instance) == null) {

                        if (beans.containsKey(field.getType().getSimpleName())) {
                            ReflectUtil.setFieldValue(field, beans.get(field.getType().getSimpleName()), instance);
                        } else {
                            isUninitializedFieldExist = true;
                        }

                    }

                }

            }

            if (isUninitializedFieldExist) {
                beanClasses.add(clazz);
            } else {
                System.out.println(String.format("生成 bean=%s", beanName));
            }
        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String beanName = entry.getKey();
            Object instance = entry.getValue();
            if (isHandler(instance.getClass())) {
                Method[] methods = instance.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String path = requestMapping.path();
                        RequestMethod requestMethod = requestMapping.method();
                        String k = String.format("%s.%s", path, requestMethod.name());
                        String v = String.format("%s.%s", beanName, method.getName());
                        routers.put(k, v);
                    }
                }
            }
        }

        System.out.println(routers);

    }

    private boolean isHandler(Class<?> clazz) {
        return clazz.isAnnotationPresent(Controller.class);
    }

}
