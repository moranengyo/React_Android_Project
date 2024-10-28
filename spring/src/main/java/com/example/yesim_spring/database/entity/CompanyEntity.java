package com.example.yesim_spring.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="company_id")
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @OneToMany(mappedBy = "company")
    @ToString.Exclude
    private List<ItemEntity> itemList = new ArrayList<>();


    public void deleteCompanyInfo(){
        name = "no data";
        email = "no data";
    }
}
