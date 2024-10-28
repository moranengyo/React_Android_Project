package com.example.yesim_spring.controller;


import com.example.yesim_spring.database.Dto.JwtDto;
import com.example.yesim_spring.database.Dto.LoginDto;
import com.example.yesim_spring.database.Dto.SignUpDto;
import com.example.yesim_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam(name="userId") String userId, @RequestParam(name = "userPw") String userPw){
        try{
            JwtDto jwtDto = userService.getAuthToken(userId, userPw);
            return ResponseEntity.ok(jwtDto);
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login false");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signUpDto){
        try{
            String resData = userService.signup(signUpDto);

            return ResponseEntity.ok(resData);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("sign up false");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken){
        try {
            String newAccessToken = userService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Expired Token");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        userService.logout();
        return null;
    }
}