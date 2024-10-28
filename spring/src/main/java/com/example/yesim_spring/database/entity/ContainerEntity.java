package com.example.yesim_spring.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "container")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContainerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "container_id")
    private long id;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private String code;

    @OneToMany(mappedBy = "container")
    @ToString.Exclude
    private List<ItemEntity> items = new ArrayList<>();
}
