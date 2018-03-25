package com.idreamshen.practice.spring.service;

import com.idreamshen.practice.spring.annotation.Component;
import com.idreamshen.practice.spring.annotation.Log;

@Component
public class Service1Impl implements Service1 {

    @Log
    public void set() {
        System.out.println("I am service1 set");
    }

}
