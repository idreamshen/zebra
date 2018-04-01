package com.idreamshen.zebra;

import com.idreamshen.zebra.bean.BeanFactory;
import com.idreamshen.zebra.controller.HomeController;
import com.idreamshen.zebra.server.NettyHttpServer;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        BeanFactory beanFactory = new BeanFactory(Application.class);
        beanFactory.initBeans();

        HomeController homeController = beanFactory.get(HomeController.class);
        System.out.println(homeController.getService1());
        System.out.println(homeController.getService2());

        NettyHttpServer server = new NettyHttpServer(8888, beanFactory.getRouters());
        server.run();

    }

}
