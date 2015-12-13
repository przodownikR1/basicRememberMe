package pl.java.scalatech.web;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
public class PreController {

    @RequestMapping("/pre")
    String pre(Principal principal) {
        return principal.getName();
    }

}
