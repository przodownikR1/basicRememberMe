package pl.java.scalatech;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.entity.Role;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.RoleRepository;
import pl.java.scalatech.repository.UserRepository;
import pl.java.scalatech.security.UserService;
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes = RememberMeApplication.class)
@WebAppConfiguration
@Slf4j
public class UserTest {
    @Autowired
    UserService userService;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    UserRepository userRepository;
    @Before
    public void setup() {
    Authentication auth = new TestingAuthenticationToken("przodownik","vava","ROLE_USER");
    SecurityContext ctx = SecurityContextHolder.getContext();
    ctx.setAuthentication(auth);
    SecurityContextHolder.setContext(ctx);
    
    Role user= roleRepository.save(new Role("USER","only for ordinary user"));
    Role admin= roleRepository.save(new Role("ADMIN","only for special right user"));
   

    User one = userRepository.save(User.builder().firstName("slawek").login("przodownik").password("vava").enabled(true).build());
    User two = userRepository.save(User.builder().firstName("vava").login("vava").password("vava").enabled(true).build());
    one.setRoles(Lists.newArrayList(user,admin));
    two.setRoles(Lists.newArrayList(user));
     User oneLoaded =  userRepository.save(one);
    User twoLoaded = userRepository.save(two);
    }
    @Test
    public void test(){
       log.info("{}",userRepository.count());
    }
    @Test
    @WithMockUser(username="przodownik",roles="ADMIN")
    public void test2(){
     
      // userService.methodAdmin2(przodownik);
    }
   
    
    @After
    public void cleanup() {
    SecurityContextHolder.clearContext();
    }

}
