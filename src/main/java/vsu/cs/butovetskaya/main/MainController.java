package vsu.cs.butovetskaya.main;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class MainController {
    @GetMapping("/test")
    public String getTestForm(@ModelAttribute Form form, Model model) {
        model.addAttribute("form", form);
        return "test";
    }

    @PostMapping("/test")
    public String submitTestForm(@ModelAttribute Form form, Model model) throws IOException, InvalidFormatException {
        model.addAttribute("form", form);
        form.checkDiabet();
        return "result";
    }
}
