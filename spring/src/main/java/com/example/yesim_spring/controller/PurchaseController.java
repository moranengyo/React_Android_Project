package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.Dto.PurchaseDto;
import com.example.yesim_spring.database.entity.define.RequestStatus;
import com.example.yesim_spring.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.yesim_spring.util.Const.*;

@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/manager/purchase/detail/{purchaseId}")
    public PurchaseDto getPurchaseDetail(@PathVariable("purchaseId") long purchaseId) {
        return purchaseService.findById(purchaseId);
    }

    @GetMapping("/manager/purchase/req/{id}")
    public ResponseEntity<?> getUserPurchaseList(@PathVariable String id){
        return ResponseEntity.ok(purchaseService.findAllByUserId(id));
    }

    @GetMapping("/manager/purchase/in-store/{pageNum}")
    public Map<String, Object> getPurchaseListInStore(@PathVariable int pageNum, @RequestParam String date){

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


        return purchaseService.findAllInStorePurchase(pageNum, dateForm);
    }

    @GetMapping("/manager/purchase/totalNum")
    public int getTotalItemNum(){
        return purchaseService.getTotalInStockCount();
    }


    @GetMapping("/manager/purchase/totalNumData")
    public Map<String, Integer> getTotalNumData(){
        return purchaseService.getPurchaseTotalNumData();
    }


    @GetMapping("/manager/purchase/status/{pageNum}")
    public Map<String, Object> getPurchaseListByStatus(@PathVariable("pageNum") int pageNum,
                                   @RequestParam String status){
        return purchaseService.findAllPurchaseByStatus(pageNum, RequestStatus.valueOf(status));
    }

    @GetMapping("/manager/purchase/in-store/search/{pageNum}")
    public Map<String,Object> getInStockPurchaseList(@PathVariable int pageNum,
                                                    @RequestParam String searchDur,
                                                    @RequestParam String searchVal){
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

        var rtn = purchaseService.getInStockPurchaseList(dateForm, searchVal, pageNum);

        return rtn;//purchaseService.getInStockPurchaseList(dateForm, searchVal, pageNum);
    }

    @GetMapping("/manager/purchase/all/{pageNum}")
    public Map<String, Object> getAllPurchaseList(@PathVariable int pageNum){
        return purchaseService.findAllPurchase(pageNum);
    }

    @GetMapping("/s-manager/purchase/unconfirmed/{pageNum}")
    public List<PurchaseDto> getUnconfirmedPurchase(@PathVariable int pageNum){

        return purchaseService.findAllUnconfirmedPurchase(pageNum);
    }

    @GetMapping("/s-manager/purchase/confirmed/{pageNum}")
    public List<PurchaseDto> getConfirmedPurchase(@PathVariable int pageNum){
        return purchaseService.findAllConfirmedPurchase(pageNum);
    }

    @PostMapping("/user/purchase/req")
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseDto purchaseDto){
        purchaseService.reqPurchase(purchaseDto);

        return ResponseEntity.ok().build();
    }
}
