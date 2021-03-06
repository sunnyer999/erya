package com.veritas.automation.erya.services.trackers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@SpringBootApplication
public class Application {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World from services.trackers micro-service";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}