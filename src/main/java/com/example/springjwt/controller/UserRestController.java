package com.example.springjwt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.entities.User;
import com.example.springjwt.exceptionjwt.InvalidUserException;
import com.example.springjwt.exceptionjwt.InvalidUserException.ErrorCode;
import com.example.springjwt.helper.CommonHelper;
import com.example.springjwt.service.JwtService;
import com.example.springjwt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/rest")
@Tag(name = "user")
public class UserRestController {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserService userService;
  @Autowired 
  private PasswordEncoder passwordEncoder;
  /* ---------------- GET ALL USER ------------------------ */
	@Operation(description = "Xem danh sách User", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Thành công"),
            @ApiResponse(responseCode  = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode  = "403", description = "Truy cập bị cấm"),
            @ApiResponse(responseCode  = "404", description = "Không tìm thấy")
    })
  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public ResponseEntity<List<User>> getAllUser() {
    return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
  }
  /* ---------------- GET USER BY ID ------------------------ */
  @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
  public ResponseEntity<Object> getUserById(@PathVariable int id) {
    User user = userService.findById(id);
    if (user != null) {
      return new ResponseEntity<Object>(user, HttpStatus.OK);
    }
    return new ResponseEntity<Object>("Not Found User", HttpStatus.NO_CONTENT);
  }
  /* ---------------- CREATE NEW USER ------------------------ */
//  @RequestMapping(value = "/users", method = RequestMethod.POST)
//  public ResponseEntity<String> createUser(@RequestBody User user) {
//    if (null != userService.add(user)) {
//      return new ResponseEntity<String>("Created!", HttpStatus.CREATED);
//    } else {
//      return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
//    }
//  }
  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public User createUser(@RequestBody User user) throws InvalidUserException{
	  User userFound = userService.findByUsername(user.getUsername());
	  if(null != userFound) {
		  throw new InvalidUserException(userFound.getUsername(), ErrorCode.EXIST);
	  }else {
		  String pass = user.getPassword();
		  user.setPassword(passwordEncoder.encode(pass));
	  }
	  if (null != userService.add(user)) {
		  return user;
	  }
	  return null;
  }
  /* ---------------- DELETE USER ------------------------ */
  @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<String> deleteUserById(@PathVariable int id) {
    userService.delete(id);
    return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
  }
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<String> login(HttpServletRequest request, @RequestBody User user) {
    String result = "";
    HttpStatus httpStatus = null;
    try {
    	if(!CommonHelper.isExistingUser(user)) {
    	      if (userService.checkUserExistingInDB(user)) {
    	          result = jwtService.generateTokenLogin(user.getUsername());
    	          httpStatus = HttpStatus.OK;
    	        } else {
    	          result = "Wrong userId and password";
    	          httpStatus = HttpStatus.BAD_REQUEST;
    	        }
    	}else {
    		result = jwtService.generateTokenLogin(user.getUsername());
	          httpStatus = HttpStatus.OK;
    	}

    } catch (Exception ex) {
      result = "Server Error";
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new ResponseEntity<String>(result, httpStatus);
  }
}
