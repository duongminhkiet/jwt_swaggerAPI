package com.example.springjwt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.springjwt.entities.Role;
import com.example.springjwt.entities.User;

@SpringBootApplication
public class SpringJwtApplication implements CommandLineRunner{
	
	public static void main(String[] args) {
		SpringApplication.run(SpringJwtApplication.class, args);
	}
    @Autowired 
    private PasswordEncoder passwordEncoder;
	public static volatile List<User> listUserGlobal = new ArrayList<User>();

	@Override
	public void run(String... args) throws Exception {
		 User admin = new User();
         admin.setUsername("admin1");
         admin.setPassword(passwordEncoder.encode("123456"));
         HashSet<Role> roles = new HashSet<>();
         roles.add(new Role("ROLE_USER"));
         roles.add(new Role("ROLE_ADMIN"));
         admin.setRoles(roles);
         
		 User u1 = new User();
		 u1.setUsername("user1");
		 u1.setPassword(passwordEncoder.encode("123456"));
         HashSet<Role> roles2 = new HashSet<>();
         roles2.add(new Role("ROLE_USER"));
         u1.setRoles(roles2);
         
         listUserGlobal.add(admin);
         listUserGlobal.add(u1);
	}
}