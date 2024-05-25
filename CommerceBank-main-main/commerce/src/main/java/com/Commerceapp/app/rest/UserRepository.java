package com.Commerceapp.app.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {
	@Query ("SELECT u FROM User u WHERE u.email = ?1")
	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.accountNum = ?1")
	Optional<User> findByAccountNum(String accountNum);

	@Query("SELECT u FROM User u WHERE u.accountNum = ?1")
	User findByAccountNum2(String accountNum);

//	@Query("SELECT u FROM User u WHERE u.password = ?1")
//	Optional<User> findByPassword(String password);

}
 