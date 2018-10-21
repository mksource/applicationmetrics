package metrics.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import metrics.model.Greeting;

@Controller
public class HelloWorldController {

    private static final String template = "Application Metrics %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Version 1") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}