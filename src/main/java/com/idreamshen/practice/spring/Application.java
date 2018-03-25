package com.idreamshen.practice.spring;

import com.idreamshen.practice.spring.bean.BeanFactory;
import com.idreamshen.practice.spring.controller.Controller;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        BeanFactory beanFactory = new BeanFactory(Application.class);
        beanFactory.initBeans();

        Controller controller = beanFactory.get(Controller.class);
        System.out.println(controller.getService1());
        System.out.println(controller.getService2());

    }

}
