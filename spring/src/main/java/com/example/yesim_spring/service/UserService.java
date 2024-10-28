package com.example.yesim_spring.service;

import com.example.yesim_spring.config.jwt.JWTProvider;
import com.example.yesim_spring.database.Dto.JwtDto;
import com.example.yesim_spring.database.Dto.NewUserDto;
import com.example.yesim_spring.database.Dto.SignUpDto;
import com.example.yesim_spring.database.entity.RefreshTokenEntity;
import com.example.yesim_spring.database.entity.UserEntity;
import com.example.yesim_spring.database.entity.define.Role;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import com.example.yesim_spring.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JWTRefreshTokenService refreshTokenService;
    private final JWTProvider provider;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder pwEncoder;
    private final UserRepository userRepository;
    private final UsageRepository usageRepository;
    private final PurchaseRepository purchaseRepository;

    public JwtDto getAuthToken(String userId, String userPw){

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, userPw)
        );

        UserEntity user = (UserEntity) auth.getPrincipal();

        Date now = new Date();
        String accessToken = provider.makeJWT(
                user,
                (new Date(now.getTime() + Duration.ofMinutes(5).toMillis()))
        );


        RefreshTokenEntity refreshToken = refreshTokenService.makeToken(user);

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public String signup(SignUpDto signUpDto){
        if(userRepository.existsByUserId(signUpDto.userId())){
            throw new IllegalArgumentException("이미 존재하는 사용자 ID 입니다");
        }

        if(userRepository.existsByEmail(signUpDto.email())){
            throw new IllegalArgumentException("이미 존재하는 사용자 Email 입니다");
        }

        String encodedPw = pwEncoder.encode(signUpDto.userPw());
        UserEntity newUser = UserEntity.builder()
                .userId(signUpDto.userId())
                .password(encodedPw)
                .userNick(signUpDto.userNick())
                .email(signUpDto.email())
                .role(Role.ROLE_USER)
                .approvedYn("N")
                .build();

        userRepository.save(newUser);

        return "success";
    }

    public String logout(){

        return "";
    }

    public String refreshAccessToken(String refreshToken){
        return refreshTokenService.findUserByToken(refreshToken)
                .map(user -> provider.makeJWT(user, Duration.ofMinutes(5)))
                .orElseThrow(() -> {
                    refreshTokenService.deleteRefreshToken(refreshToken);
                    return new IllegalArgumentException("RefreshToken is Expired");
                });
    }

    public Map<String, Object> findAllUnconfirmedUser(int pageNumber){
        var page = userRepository.getUnconfirmedUserList(PageRequest.of(pageNumber, 10));
        var unconfirmedUserList = page.stream().map(NewUserDto::of).toList();
        int totalPages = page.getTotalPages();

        Map<String, Object> data = new HashMap<>();

        data.put("unconfirmedUserList", unconfirmedUserList);
        data.put("totalPages", totalPages);

        return data;
    }

    public Map<String, Object> findAllApprovedUser(int pageNumber){
        var page = userRepository.getApprovedUserList(PageRequest.of(pageNumber, 10));
        var approvedUserList = page.stream().map(NewUserDto::of).toList();
        int totalPages = page.getTotalPages();

        Map<String, Object> data = new HashMap<>();

        data.put("approvedUserList", approvedUserList);
        data.put("totalPages", totalPages);

        return data;
    }

    public String changeRole(long id, Role newRole){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));

        user.setRole(newRole);
        userRepository.save(user);

        return newRole.toString();
    }

    public void approveUser(long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));

        user.setApprovedYn("Y");

        userRepository.save(user);
    }

    public void rejectUser(long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));
        deleteUser(user.getUserId());
    }

    public void deleteUser(String userId){
        var removedUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("not found " + userId));

        var usageList = usageRepository.findAllByUser_UserId(userId);
        var purchaseList = purchaseRepository.findAllByUser_UserId(userId);

        for(var usage : usageList){
            usage.ChangeUser(removedUser);
        }

        for(var purchase : purchaseList){
            purchase.ChangeUser(removedUser);
        }

        usageRepository.saveAll(usageList);
        purchaseRepository.saveAll(purchaseList);

        userRepository.deleteByUserId(userId);
    }
}