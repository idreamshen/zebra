package com.idreamshen.practice.spring;

import com.idreamshen.practice.spring.annotation.Autowired;
import com.idreamshen.practice.spring.annotation.Log;

//@Component
public class ControllerImpl implements Controller {

    @Autowired
    Manager manager;

    @Log
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
