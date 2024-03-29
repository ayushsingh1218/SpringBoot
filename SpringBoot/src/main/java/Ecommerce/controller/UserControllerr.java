package Ecommerce.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import Ecommerce.dto.UserDtoLogin;

import Ecommerce.entity.User;

import Ecommerce.service.SessionService;
import Ecommerce.service.UserService;
import lombok.AllArgsConstructor;


import java.util.List;

import java.util.Optional;


@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserControllerr 
{

    private final UserService userService;
   
    
        

    // private final Map<String, User> loggedInUsers = new HashMap<>();

       private final SessionService sessionService;

    // User Registration
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Retrieve User by ID
    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Retrieve All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update User
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Delete User
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Successfully deleted!", HttpStatus.OK);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDtoLogin userDtoLogin) {
        Optional<User> optionalUser = userService.authenticate(userDtoLogin.getEmail(), userDtoLogin.getPassword());
        if (optionalUser.isPresent()) {
            String sessionId = sessionService.generateSessionId();
            sessionService.loggedInUsers.put(sessionId, optionalUser.get());
            return ResponseEntity.ok(sessionId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password");
        }
    }

   

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String sessionId) {
        sessionService.loggedInUsers.remove(sessionId);
        return ResponseEntity.ok("Logged out successfully");
    }
}
