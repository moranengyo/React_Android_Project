package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);

    Boolean existsByUserId(String userId);

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByUserIdAndApprovedYn(String userId, String approvedYn);

    int countAllBySeqNotAndApprovedYn(Long seq, String yn);


    @Query(value = "select * from user where approved_yn = \"N\" order by user_seq DESC ", nativeQuery = true)
    Page<UserEntity> getUnconfirmedUserList(Pageable pageable);

    @Query(value = "select * from user where approved_yn = \"Y\" " +
            "and not(user_seq = 1) " +
            "and user_name like concat('%', concat(:userName, '%')) " +
            "order by case when role = \"ROLE_SENIOR_MANAGER\" then 0 when role = \"ROLE_MANAGER\" then 1 else 2 end", nativeQuery = true)
    Page<UserEntity> getApprovedUserList(Pageable pageable, @Param("userName") String userName);


    void deleteByUserId(String userId);
}
