package com.sparta.wuzuzu.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/chatting")
    public String chatting() {
        return "chatting";
    }

    @GetMapping("/map")
    public String map() {
        return "map";
    }
}
