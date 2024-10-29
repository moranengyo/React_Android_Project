package com.example.yesim_spring.controller;


import com.example.yesim_spring.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.yesim_spring.util.Const.*;

@RestController
@RequiredArgsConstructor
public class UsageController {
    private final UsageService usageService;

    @GetMapping("/manager/usage/search/{pageNum}")
    public Map<String, Object> getUsageSearchList(@PathVariable int pageNum,
                                                  @RequestParam String searchDur,
                                                  @RequestParam String searchVal) {

        String dateForm = "";
        switch (searchDur) {
            case "Y":
                dateForm = QUERY_DATE_FORMAT_YEAR;
                break;
            case "M":
                dateForm = QUERY_DATE_FORMAT_MONTH;
                break;
            default:
                dateForm = QUERY_DATE_FORMAT_DAY;
        }
        var data = usageService.getUsageSearchList(pageNum, dateForm, searchVal);
        return data;//usageService.getUsageSearchList(pageNum, dateformat, searchVal);
    }

    @GetMapping("/manager/usage/{pageNum}")
    public Map<String, Object> getUsageSearchList(@PathVariable int pageNum,
                                                  @RequestParam String date) {
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

        return usageService.getTotalUsageListByDate(pageNum, dateForm);
    }

    @GetMapping("/user/usage/item/{pageNum}")
    public Map<String, Object> getTotalUsageListByDateAndGroupedItem(@PathVariable int pageNum,
                                                                     @RequestParam String date) {
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

        return usageService.getTotalUsageListByDateAndGroupedItem(pageNum, dateForm);
    }

    @GetMapping("/user/usage/user/item/{pageNum}")
    public Map<String, Object> getTotalUsageListByDateAndUserGroupedItem(@PathVariable int pageNum,
                                                                     @RequestParam String date,
                                                                         @RequestParam String userId) {
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

        return usageService.getTotalUsageListByDateAndUserGroupedItem(pageNum, dateForm, userId);
    }

    @GetMapping("/user/usage/user/{pageNum}")
    public Map<String, Object> getTotalUsageListByDateAndUser(@PathVariable int pageNum,
                                                              @RequestParam String date) {
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

        return usageService.totalCountUsageByDateGroupedUserAndItem(pageNum, dateForm);
    }

}
