package com.idreamshen.practice.spring.controller;

import com.idreamshen.practice.spring.annotation.Autowired;
import com.idreamshen.practice.spring.annotation.Component;
import com.idreamshen.practice.spring.annotation.Log;
import com.idreamshen.practice.spring.service.Service1;
import com.idreamshen.practice.spring.service.Service2;

@Component
public class ControllerImpl implements Controller {

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
}
