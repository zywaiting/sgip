package com.zy.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/ceshi")
public class SgipController {

    @Resource
    private SgipService sgipService;

    @RequestMapping(value = "/smsSend")
    @ResponseBody
    public String smsSend(HttpServletRequest request) {
        return sgipService.smsSend(request);
    }
}
