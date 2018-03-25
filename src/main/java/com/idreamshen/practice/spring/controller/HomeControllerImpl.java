package com.idreamshen.practice.spring.controller;

import com.idreamshen.practice.spring.annotation.Autowired;
import com.idreamshen.practice.spring.annotation.Component;
import com.idreamshen.practice.spring.annotation.Controller;
import com.idreamshen.practice.spring.annotation.RequestMapping;
import com.idreamshen.practice.spring.enums.RequestMethod;
import com.idreamshen.practice.spring.service.Service1;
import com.idreamshen.practice.spring.service.Service2;

@Controller
public class HomeControllerImpl implements HomeController {

    @Autowired
    Service1 service1;

    @Autowired
    Service2 service2;


    @Override
    public Service1 getService1() {
        return service1;
    }

    @Override
    public Service2 getService2() {
        return service2;
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(path = "/items", method = RequestMethod.GET)
    public String items() {
        return "items";
    }
}
