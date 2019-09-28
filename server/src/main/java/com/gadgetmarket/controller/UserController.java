package com.gadgetmarket.controller;

import com.gadgetmarket.form.UserForm;
import com.gadgetmarket.model.User;
import com.gadgetmarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        log.trace("Call getAllUsers method");

        List<User> users = this.userRepository.findAll();

        if (users.isEmpty()) {
            log.trace("User is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") final Long id) {

        log.trace("Call getUser method");

        User user = userRepository.findUserById(id);

        if (user == null) {
            log.trace("User is null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody @Valid final UserForm userForm) {

        log.trace("Call saveUser method");

        if (userForm == null) {
            log.trace("UserForm is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userForm.toUser();
        this.userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody @Valid final User user,
                                           @PathVariable final Long id) {

        log.trace("Call updateUser method");

        if (user == null) {
            log.trace("User is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!userRepository.existsById(id)) {
            log.trace("User doesn't exists");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.setId(id);
        this.userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") final Long id) {

        log.trace("Call deleteUser method");

        User user = this.userRepository.findUserById(id);

        if (user == null) {
            log.trace("User is null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userRepository.deleteUserById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}