package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);

    Boolean existsByUserId(String userId);

    Boolean existsByEmail(String email);

    @Query(value = "select * from user where approved_yn = \"N\" order by user_seq DESC ", nativeQuery = true)
    Page<UserEntity> getUnconfirmedUserList(Pageable pageable);

    @Query(value = "select * from user where approved_yn = \"Y\" order by case when role = \"ROLE_SEINIOR_MANAGER\" then 0 when role = \"ROLE_MANAGER\" then 1 else 2 end", nativeQuery = true)
    Page<UserEntity> getApprovedUserList(Pageable pageable);


    void deleteByUserId(String userId);
}
