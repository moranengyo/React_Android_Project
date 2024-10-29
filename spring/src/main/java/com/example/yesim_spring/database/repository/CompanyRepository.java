package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{

    Page<CompanyEntity> findAllByNameNot(String companyName, Pageable pageable);

    int countAllByNameNot(String companyName);

    List<CompanyEntity> findAllByNameContaining(String searchVal);
}
