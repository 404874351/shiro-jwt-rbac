package com.zjc.shiro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Transactional(rollbackFor={Exception.class})
@RestController()
@RequestMapping("/test")
public class TestController {

    @GetMapping("/1")
    public String test1() {
        return "test1";
    }

}
