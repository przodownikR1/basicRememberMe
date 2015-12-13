package pl.java.scalatech.security;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.annotation.SecurityComponent;
import pl.java.scalatech.entity.User;
import pl.java.scalatech.repository.UserRepository;

@SecurityComponent
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void methodAdmin(String msg) {
      log.info("++ admin {}",msg);
        
    }

    @Override
    public void methodAdmin2(User user) {
        log.info("{}",userRepository.findByLogin(user.getLogin()));
        log.info("++ admin2 {}", user);
        
    }

    @Override
    public User methodAdminRet() {
        log.info("admin ret");
        return userRepository.findByLogin("przodownik").get();
    }

}
