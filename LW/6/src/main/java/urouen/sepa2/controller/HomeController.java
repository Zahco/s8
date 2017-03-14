package urouen.sepa2.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
   
	@RequestMapping(value = "/", method = RequestMethod.GET)
   public @ResponseBody String home() {
       return "<b>Hello</b> ";
    }
}