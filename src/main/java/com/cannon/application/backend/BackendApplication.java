package com.cannon.application.backend;

import com.cannon.application.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class BackendApplication {


	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void getBearer(){
		userService.getBearer();

	}
}


