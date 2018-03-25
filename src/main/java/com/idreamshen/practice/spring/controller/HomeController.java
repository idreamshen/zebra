package com.idreamshen.practice.spring.controller;

import com.idreamshen.practice.spring.service.Service1;
import com.idreamshen.practice.spring.service.Service2;

public interface HomeController {

    Service1 getService1();

    Service2 getService2();
}
