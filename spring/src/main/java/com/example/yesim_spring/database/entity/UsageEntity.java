package com.example.yesim_spring.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name= "item_usage")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private long id;


    @Column(nullable = false)
    private String totalCode;

    @Column(nullable = false)
    private int usageNum;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime usageTime;

    @ManyToOne
    @JoinColumn(name = "user_seq", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    private ItemEntity item;

    public void ChangeUser(UserEntity user) {
        this.user = user;
    }
}
