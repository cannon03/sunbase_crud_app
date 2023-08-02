package com.cannon.application.backend.controllers;

import com.cannon.application.backend.dto.UseroDto.UserDto;
import com.cannon.application.backend.model.User;
import com.cannon.application.backend.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController()
@RequestMapping("/api")
public class UserController {


    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public String getBearer(){

        return userService.getBearer();

    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getStream")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(){

        return userService.getUsers();

    }

    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestParam String uuid){
        return userService.removeUser(uuid);
    }
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> create(@RequestBody UserDto userDto){
        return  userService.createUser(userDto);
    }

    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> edit(@RequestParam String uuid, @RequestBody UserDto userDto){
        return userService.editUser(uuid,userDto);
    }


}
