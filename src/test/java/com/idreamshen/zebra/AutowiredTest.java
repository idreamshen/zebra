package com.idreamshen.zebra;

import com.idreamshen.zebra.bean.BeanFactory;
import com.idreamshen.zebra.controller.IndexController;
import com.idreamshen.zebra.service.GoldService;
import org.junit.Assert;
import org.junit.Test;

public class AutowiredTest {

    @Test
    public void sameBeanShouldEqual() {
        BeanFactory beanFactory = new BeanFactory(AutowiredTest.class);
        beanFactory.initBeans();

        IndexController indexController = beanFactory.get(IndexController.class);
        GoldService goldService = beanFactory.get(GoldService.class);
        Assert.assertTrue(indexController.getGoldService().equals(goldService));
    }

}
