package com.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/test")
    public String getTestForm(@ModelAttribute Form form, Model model) {
        model.addAttribute("form", form);
        return "test";
    }

    /*
    * Здесь отправка формы.
    * Можно вытаскивать все введенные значения,
    * передавать их в алгоритм.
    * Результат алгоритма использовать на странице result.html
    * */
    @PostMapping("/test")
    public String submitTestForm(@ModelAttribute Form form, Model model) {
        form.setResult(1.0F);
        model.addAttribute("form", form);
        return "result";
    }
}
