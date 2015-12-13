package pl.java.scalatech.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.security.UserService;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

  /*  @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;*/

    @RequestMapping("/adminTest")
    String user(Principal user){
        userService.methodAdmin("test");
        return user.getName();
    }
    @RequestMapping("/adminTest2")
    String user2(Principal user){
        userService.methodAdmin2(userRepository.findByLogin(user.getName()).get());
        return user.getName();
    }

   @RequestMapping("/adminTest3")
    User userRet(Principal principal){
        User ud = userRepository.findByLogin(principal.getName()).get();
        return ud ;
    }

   @RequestMapping("/adminTest4")
   String user4(Principal user){

       return "ok";
   }
}
