package com.example.yesim_spring.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private long id;

    @Column(nullable = false, unique = true)
    private String totalCode;

    @Column(nullable = false, name = "`name`", unique = true)
    private String name;

    @Column(nullable = false)
    private int totalNum;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int minNum;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int price;

    @Column(length = 2000, nullable = false)
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "container_id", nullable = false)
    @ToString.Exclude
    private ContainerEntity container;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @ToString.Exclude
    private CompanyEntity company;

    @OneToMany(mappedBy = "item")
    @ToString.Exclude
    private List<UsageEntity> usageList = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    @ToString.Exclude
    private List<PurchaseEntity> purchaseRequests = new ArrayList<>();


    public boolean useItem(int usageNum){
        if(totalNum < usageNum) return false;
        totalNum -= usageNum;

        return true;
    }

    public void inStock(int count) {
        totalNum += count;
    }

    public void addCode(String code) {
        totalCode = code;
    }
}
