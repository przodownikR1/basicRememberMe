package pl.java.scalatech.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DefaultController {
    @RequestMapping("/default")
    String user(Principal user) {
        log.info("+++++++  {}",user);
        return "default";
    }
}
