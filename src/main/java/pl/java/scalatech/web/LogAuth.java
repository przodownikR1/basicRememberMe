package pl.java.scalatech.web;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogAuth {
    @RequestMapping("/logAuth/{name}/password/{passwd}")
    public String  login(@PathVariable("name") String name,@PathVariable("passwd")String passwd) {
    Authentication auth  = new UsernamePasswordAuthenticationToken(name, passwd,AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(auth);


   return "welcome";
}
}
