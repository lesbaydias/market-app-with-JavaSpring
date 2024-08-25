package com.example.marketAppWithJavaSpring;

import com.example.marketAppWithJavaSpring.enums.TypeOfUser;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class MarketAppWithJavaSpring implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args){
		SpringApplication.run(MarketAppWithJavaSpring.class, args);
	}

	@Override
	public void run(String... args){
		User adminAccount = userRepository.findUsersByStatus(TypeOfUser.ADMIN);
		if(adminAccount == null){
			User user = new User();
			user.setUsername("admin");
			user.setStatus(TypeOfUser.ADMIN);
			user.setEmail("admin@gmail.com");
			user.setLastname("admin");
			user.setFirstname("admin");
			user.setAddress("ALA");
			user.setPhoneNumber("+7737-233-21-21");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}
}
