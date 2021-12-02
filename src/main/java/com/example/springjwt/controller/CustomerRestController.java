package com.example.springjwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.entities.User;
import com.example.springjwt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {
	  @Autowired
	  private UserService userService;
	  /* ---------------- GET ALL USER ------------------------ */
		@Operation(description = "Xem danh sách Book", responses = {
	            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200") })
	    @ApiResponses(value = {
	            @ApiResponse(responseCode  = "200", description = "Thành công"),
	            @ApiResponse(responseCode  = "401", description = "Chưa xác thực"),
	            @ApiResponse(responseCode  = "403", description = "Truy cập bị cấm"),
	            @ApiResponse(responseCode  = "404", description = "Không tìm thấy")
	    })
	  @Secured("ROLE_ADMIN")
	  @RequestMapping(value = "/users", method = RequestMethod.GET)
	  public ResponseEntity<List<User>> getAllUser() {
	    return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
	  }
}