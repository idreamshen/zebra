package com.idreamshen.zebra.controller;

import com.idreamshen.zebra.annotation.Autowired;
import com.idreamshen.zebra.annotation.Controller;
import com.idreamshen.zebra.annotation.RequestMapping;
import com.idreamshen.zebra.enums.RequestMethod;
import com.idreamshen.zebra.service.Service1;
import com.idreamshen.zebra.service.Service2;

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
