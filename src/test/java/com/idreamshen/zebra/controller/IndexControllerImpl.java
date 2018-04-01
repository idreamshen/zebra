package com.idreamshen.zebra.controller;

import com.idreamshen.zebra.annotation.Autowired;
import com.idreamshen.zebra.annotation.Controller;
import com.idreamshen.zebra.service.GoldService;

@Controller
public class IndexControllerImpl implements IndexController {

    @Autowired
    private GoldService goldService;

    public GoldService getGoldService() {
        return goldService;
    }
}
