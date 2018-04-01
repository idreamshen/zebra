package com.idreamshen.zebra.service;

import com.idreamshen.zebra.annotation.Component;
import com.idreamshen.zebra.annotation.Log;

@Component
public class Service1Impl implements Service1 {

    @Log
    public void set() {
        System.out.println("I am service1 set");
    }

}
