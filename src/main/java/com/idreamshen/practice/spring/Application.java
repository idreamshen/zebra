package com.idreamshen.practice.spring;

import com.idreamshen.practice.spring.bean.BeanFactory;
import com.idreamshen.practice.spring.controller.HomeController;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        BeanFactory beanFactory = new BeanFactory(Application.class);
        beanFactory.initBeans();

        HomeController homeController = beanFactory.get(HomeController.class);
        System.out.println(homeController.getService1());
        System.out.println(homeController.getService2());



    }

}
