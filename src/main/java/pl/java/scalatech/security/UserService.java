package pl.java.scalatech.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import pl.java.scalatech.entity.User;

public interface UserService {
		
	@PreAuthorize("hasRole('ADMIN')")
	public void methodAdmin(String msg);
	
	@PreAuthorize ("#user.userName == authentication.name")
	public void methodAdmin2(User user);

	@PostAuthorize ("returnObject.userName == authentication.name")
	public User methodAdminRet();
}