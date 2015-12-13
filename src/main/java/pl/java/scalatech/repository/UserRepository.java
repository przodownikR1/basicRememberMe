package pl.java.scalatech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import pl.java.scalatech.entity.User;

@Transactional
public interface UserRepository extends JpaRepository<User,Long>{
  Optional<User> findByLogin(String login);

 // @PreAuthorize("hasRole('ADMIN')")
  //@PostAuthorize("returnObject?.login==authentication.name")
  User findByFirstNameLike(String name);
}
//@PostAuthorize("returnObject.user==principal or hasRole('ADMIN')")

//@PreAuthorize("hasRole('ROLE_ADMIN') or ( isAuthenticated() and #id == principal.id )")
//User findOne(Long id);