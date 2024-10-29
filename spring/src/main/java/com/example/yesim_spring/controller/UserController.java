package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.entity.define.Role;
import com.example.yesim_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping({"/s-manager/unconfirmed-user", "/s-manager/unconfirmed-user/"})
    public ResponseEntity<?> getUnconfirmedUser() {

        return ResponseEntity.ok(userService.findAllUnconfirmedUser(0));
    }

    @GetMapping({"/s-manager/unconfirmed-user/{pageNumber}"})
    public ResponseEntity<?> getUnconfirmedUser(@PathVariable int pageNumber) {
        return ResponseEntity.ok(userService.findAllUnconfirmedUser(pageNumber));
    }


    @GetMapping("/s-manager/approved-user/{pageNumber}")
    public ResponseEntity<?> getApprovedUser(@PathVariable int pageNumber, @RequestParam String userName) {
        return ResponseEntity.ok(userService.findAllApprovedUser(pageNumber, userName));
    }



    @PutMapping("/s-manager/user/changeRole/{id}")
    public ResponseEntity<?> changUserRole(@PathVariable int id, @RequestParam String role) {
        try {
            userService.changeRole(id, Role.valueOf(role));
            return ResponseEntity.ok("Y");
        }
        catch (Exception e){
            return ResponseEntity.ok("N");
        }
    }

    @PutMapping("/s-manager/user/approved/{id}")
    public ResponseEntity<?> approveUser(@PathVariable long id){
        try {
            userService.approveUser(id);
            return ResponseEntity.ok("Y");
        }catch (Exception e){
            return ResponseEntity.ok("N");
        }
    }

    @DeleteMapping("/s-manager/user/reject/{id}")
    public ResponseEntity<?> rejectUser(@PathVariable long id){
        try {
            userService.rejectUser(id);
            return ResponseEntity.ok("Y");
        }catch (Exception e){
            return ResponseEntity.ok("N");
        }
    }

    @DeleteMapping("/s-manager/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Y");
        }catch (Exception e){
            return ResponseEntity.ok("N");
        }
    }

}