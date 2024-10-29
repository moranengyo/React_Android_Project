package com.example.yesim_spring.database.entity;

import com.example.yesim_spring.database.entity.define.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="purchase")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int reqNum;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime reqTime;

    @Column
    private LocalDateTime approvedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus approvedStatus;

    @Column
    private String reqComment;

    @Column
    private String approvalComment;

    @Column(nullable = false)
//    @ColumnDefault("N")
    private String newYn;

    @ManyToOne
    @JoinColumn(name="user_seq", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @Setter
    @ManyToOne
    @JoinColumn(name="item_id", nullable = false)
    @ToString.Exclude
    private ItemEntity item;


    public void ChangeUser(UserEntity user) {
        this.user = user;
    }

    public void updateStatusAndTime(RequestStatus status) {
        approvedStatus = status;
        approvedTime = LocalDateTime.now();
    }

    public void rejectPurchase(String approvalComment){
        this.approvedStatus = RequestStatus.CANCEL;
        this.approvalComment = approvalComment;
        approvedTime = LocalDateTime.now();
    }


    public void approvePurchase(String approvalComment){
        this.approvedStatus = RequestStatus.APPROVE;
        this.approvalComment = approvalComment;
        approvedTime = LocalDateTime.now();
    }
}
