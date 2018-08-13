package com.yz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yz
 * @Description
 * @Date create by 13:51 18/8/1
 */
@Controller
public class IndexConroller {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
