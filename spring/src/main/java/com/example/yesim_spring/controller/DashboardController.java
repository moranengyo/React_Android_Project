package com.example.yesim_spring.controller;


import com.example.yesim_spring.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.yesim_spring.util.Const.*;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashBoardService dashBoardService;


    @GetMapping("/manager/dashboard")
    public Map<String, Object> getDashBoardInoutCnt(@RequestParam String date){
        String dateForm = "";
        switch (date) {
            case "Y":
                dateForm = QUERY_DATE_FORMAT_YEAR;
                break;
            case "M":
                dateForm = QUERY_DATE_FORMAT_MONTH;
                break;
            default:
                dateForm = QUERY_DATE_FORMAT_DAY;
        }


        return dashBoardService.dashBoardInoutCnt(dateForm);
    }

    @GetMapping("/manager/dashboard/purchaseCnt")
    public Map<String, Object> getDashBoardPurchaseCnt(){

        return dashBoardService.dashBoardPurchaseCnt();
    }
}
