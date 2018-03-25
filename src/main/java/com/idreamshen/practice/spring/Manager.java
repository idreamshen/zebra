package com.idreamshen.practice.spring;

import com.idreamshen.practice.spring.annotation.Autowired;

//@Component
public class Manager {

    @Autowired
    Service1 service1;

    @Autowired
    Service2 service2;

    public Service1 getService1() {
        return service1;
    }

    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    public Service2 getService2() {
        return service2;
    }

    public void setService2(Service2 service2) {
        this.service2 = service2;
    }
}
