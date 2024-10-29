package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.Dto.CompanyDto;
import com.example.yesim_spring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;


    @GetMapping("/manager/company/all/{pageNum}")
    public Map<String, Object> getCompanyList(@PathVariable int pageNum) {
        return companyService.findAll(pageNum);
    }

    @PostMapping("/manager/company/new")
    public void addNewCompany(@RequestBody CompanyDto companyDto){
        companyService.saveCompany(companyDto);
    }

    @PutMapping("/manager/company/update/{companyId}")
    public void updateCompany(@PathVariable int companyId, @RequestBody CompanyDto companyDto){
        companyService.saveCompany(companyDto);
    }

    @DeleteMapping("/manager/company/delete/{companyId}")
    public void deleteCompany(@PathVariable int companyId){
        companyService.DeleteCompany(companyId);
    }


    @GetMapping("/manager/company/search")
    public List<CompanyDto> getSearchCompanyList(@RequestParam String searchVal){
        return companyService.getSearchCompanyList(searchVal);
    }
}
