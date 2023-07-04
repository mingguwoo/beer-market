package com.nio.ngfs.plm.bom.configuration.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luke.zhu
 * @date 01/30/2023
 */
@RestController
@RequestMapping("/")
public class ProbeController {

    @GetMapping(value = {"/status","/health"})
    public String healthCheck(){
        return "ok";
    }
}
