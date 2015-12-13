package pl.java.scalatech.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Admin2Controller {

    @RequestMapping("/userOne")
    String ret(){
       return  "ok";
    }
    
    @RequestMapping("/ip")
    String ip(){
       return  "ok";
    }
    
}
