package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.CompanyDto;
import com.example.yesim_spring.database.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Map<String, Object> findAll(int pageNum){
        Map<String, Object> result = new HashMap<>();

        result.put("totalCompanyCount", companyRepository.countAllByNameNot("no data"));
        result.put("companyList", companyRepository.findAllByNameNot("no data", PageRequest.of(pageNum, 10))
                .stream().map(CompanyDto::of).toList());

        return result;
    }

    public void saveCompany(CompanyDto companyDto){
        companyRepository.save(CompanyDto.toEntity(companyDto));
    }

    public void DeleteCompany(long companyId){
        var company = companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("not found"));

        if(company.getItemList().isEmpty()){
            companyRepository.delete(company);
        }else {
            company.deleteCompanyInfo();
            companyRepository.save(company);
        }
//        companyRepository.deleteById(companyId);
    }


    public List<CompanyDto> getSearchCompanyList(String searchVal){
        return companyRepository.findAllByNameContaining(searchVal)
                .stream().map(CompanyDto::of).toList();
    }
}
