package com.idreamshen.practice.spring;

import com.idreamshen.practice.spring.annotation.Autowired;
import com.idreamshen.practice.spring.annotation.Component;
import com.idreamshen.practice.spring.bean.BeanFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FakeSpring {

    private static Map<String, Object> beans = new HashMap<>();
    private static Queue<Class> classes = new LinkedList<>();


    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        BeanFactory beanFactory = new BeanFactory(FakeSpring.class);
        beanFactory.initBeans();

        URL url = Thread.currentThread().getContextClassLoader().getResource("com/idreamshen/practice/spring");

        System.out.println(url.getPath());

        File dir = new File(url.getPath());

        File[] files = dir.listFiles();
        for (File file : files) {
            String tmp = file.getAbsolutePath().split("classes")[1].substring(1);
            tmp = tmp.substring(0, tmp.length() - 6);
            String className = tmp.replaceAll("/", ".");
            Class clazz = Class.forName(className);

            if (isComponent(clazz)) {
                classes.add(clazz);
            }
        }

        while (!classes.isEmpty()) {
            Class clazz = classes.poll();
            String beanName = clazz.getSimpleName();
            if (beanName.endsWith("Impl")) {
                beanName = beanName.replace("Impl", "");
            }

            if (!beans.containsKey(beanName)) {

                Object realInstance = clazz.newInstance();

                if (beanName.equals("Service1")) {
                    DefaultProxyHandler handler = new DefaultProxyHandler(realInstance);
                    Object proxyInstance = Proxy.newProxyInstance(realInstance.getClass().getClassLoader(), realInstance.getClass().getInterfaces(), handler);
                    beans.put(beanName, proxyInstance);
                } else {
                    beans.put(beanName, realInstance);
                }

            }

            Object instance = beans.get(beanName);

            if (Proxy.isProxyClass(instance.getClass())) {
            }

            Field[] fields = instance.getClass().getDeclaredFields();

            boolean isUninitializedFieldExist = false;

            for (Field field : fields) {

                if (field.isAnnotationPresent(Autowired.class)) {

                    if (field.get(instance) == null) {
                        
                        if (beans.containsKey(field.getType().getSimpleName())) {
                            field.setAccessible(true);
                            field.set(instance, beans.get(field.getType().getSimpleName()));
                            field.setAccessible(false);
                        } else {
                            isUninitializedFieldExist = true;
                        }
                        
                    }

                }

            }
            
            if (isUninitializedFieldExist) {
                classes.add(clazz);
            }
        }

        System.out.println(beans);

        Service1 service1 = (Service1)beans.get("Service1");
        Service2 service2 = (Service2)beans.get("Service2");
        Manager manager = (Manager)beans.get("Manager");
        Controller controller = (Controller)beans.get("Controller");
        System.out.printf("[Service1]: %s\n", service1);
        System.out.printf("[Service2]: %s\n", service2);
        service1.set();
        System.out.printf("[Manager]: %s, %s, %s\n", manager, manager.getService1(), manager.getService2());
        System.out.printf("[Controller]: %s, %s, %s, %s\n", controller, controller.getManager(), controller.getManager().getService1(), controller.getManager().getService2());
    }

    private static boolean isComponent(Class clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }



}
