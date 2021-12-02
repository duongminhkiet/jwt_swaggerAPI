package com.example.springjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.springjwt.rest.CustomAccessDeniedHandler;
import com.example.springjwt.rest.JwtAuthenticationTokenFilter;
import com.example.springjwt.rest.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)// to enable for adding @Secured at controller
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	    
	}
//	@Autowired
//	private UserDetailsService userDetailsService;
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

	
	//
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails user1 = User
//                .withUsername("user")
//                .password("123456")//("$2a$10$sWszOXuTlN0amQi8vXp4cerb.tJUQo.4FzLAnTCsSqChsYhlLdQWW")
//                .roles("USER")
//                .build();
//        UserDetails user2 = User
//                .withUsername("admin")
//                .password("123456")//("$2a$10$PH0p2x2x8oi5bKx.80Bt7ubMAiHdZnqm9TC/Cpss9VoccyTYw1AoC")
//                .roles("ADMIN")
//                .build();      
//         
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
//    @Autowired 
//    private PasswordEncoder passwordEncoder;
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
// 
//        //User Role
//        UserDetails theUser = User.withUsername("u1")
//                        .password(passwordEncoder().encode("123456")).roles("USER").build();
//        
//        //Manager Role 
//        UserDetails theManager = User.withUsername("a1")
//                .password(passwordEncoder().encode("123456")).roles("ADMIN").build();
//        
//  
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//              
//        userDetailsManager.createUser(theUser);
//        userDetailsManager.createUser(theManager);
//        
//        return userDetailsManager;
//    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//        .withUser("u2")
//        .password("{bcrypt}123456")
//        .roles("USER")
//        .and()
//        .withUser("a2")
//        .password("{bcrypt}123456")
//        .roles("ADMIN");
//    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("u2")
//                .password("{bcrypt}123456")
//                .roles("USER")
//                .and()
//                .withUser("a2")
//                .password("{bcrypt}123456")
//                .roles("ADMIN");
//    }
	//
    
	@Bean
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
		JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
		jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager());
		return jwtAuthenticationTokenFilter;
	}

	@Bean
	public RestAuthenticationEntryPoint restServicesEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}


	protected void configure(HttpSecurity http) throws Exception {
		// Disable crsf cho đường dẫn /rest/**
		http.csrf().ignoringAntMatchers("/rest/**");
		http.authorizeRequests().antMatchers("/rest/login**").permitAll();
		http.antMatcher("/rest/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/rest/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
				.antMatchers(HttpMethod.GET, "/customer/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
				.antMatchers(HttpMethod.POST, "/rest/**").access("hasRole('ROLE_ADMIN')")
				.antMatchers(HttpMethod.DELETE, "/rest/**").access("hasRole('ROLE_ADMIN')").and()
				.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
	}
}
