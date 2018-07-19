package com.hemonth.restcontroller;

import com.hemonth.jmsclient.JmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Autowired
    JmsClient jsmClient;

    @RequestMapping(path="/produce", method = RequestMethod.GET)
    public String produce(@RequestParam("msg")String msg){
        jsmClient.send(msg);
        return "Done";
    }

    @RequestMapping(path = "/wish", method = RequestMethod.GET)
    public String greeting(){
        return "good morning!!";
    }

    @RequestMapping(path="/receive", method = RequestMethod.GET)
    public String receive(){
        return jsmClient.receive();
    }
}
