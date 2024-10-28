package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.PurchaseDto;
import com.example.yesim_spring.database.entity.PurchaseEntity;
import com.example.yesim_spring.database.entity.define.RequestStatus;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.sql.init.dependency.DatabaseInitializationDependencyConfigurer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;


    public PurchaseDto findById(Long id){
        var purchaseDetail = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("now found purchase"));

        return PurchaseDto.of(purchaseDetail);
    }

    public List<PurchaseDto> findAll() {
        return purchaseRepository.findAll()
                .stream().map(PurchaseDto::of).toList();
    }

    public Map<String, Object> findAllPurchase(int pageNum){
        Map<String, Object> data = new HashMap<>();

        data.put("totalPurchaseCnt", purchaseRepository.count());
        data.put("purchaseList", purchaseRepository.findAll(PageRequest.of(pageNum, 4))
                .stream().map(PurchaseDto::of).toList());

        return data;
    }

    public Map<String, Object> findAllPurchaseByStatus(int pageNum, RequestStatus status){
        Map<String, Object> data = new HashMap<>();


        data.put("totalPurchaseCnt", purchaseRepository.countAllByApprovedStatus(status));
        data.put("purchaseList", purchaseRepository.findAllByApprovedStatus(status, PageRequest.of(pageNum, 4))
                .stream().map(PurchaseDto::of).toList());

        return data;
    }

    public List<PurchaseDto> findAllByUserId(String id){
        return purchaseRepository.findAllByUser_UserId(id).stream().map(PurchaseDto::of).toList();
    }




    public int getTotalInStockCount(){
        return purchaseRepository.getTotalInStockCount();
    }

    public Map<String, Integer> getPurchaseTotalNumData(){
        var puchaseList = purchaseRepository.findAll();

        int wait = 0;
        int approved = 0;
        int cancel = 0;

        for(var purchase : puchaseList){
            switch(purchase.getApprovedStatus()){
                case WAIT:
                case UNCONFIRMED:
                    wait++;
                    break;

                case APPROVE:
                    approved++;
                    break;

                case CANCEL:
                    cancel++;
                    break;
            }
        }

        Map<String, Integer> data = new HashMap<>();
        data.put("wait", wait);
        data.put("approved", approved);
        data.put("cancel", cancel);

        return data;
    }

    public Map<String,Object> getInStockPurchaseList(String dateformat, String searchValue, int pageNum){
        Map<String,Object> data = new HashMap<>();
        data.put("searchResultCount", purchaseRepository.getSearchInStockPurchaseCountByDate(dateformat, searchValue));
        data.put("searchResult", purchaseRepository.getInStockPurchaseList(dateformat, searchValue, PageRequest.of(pageNum, 10))
                .stream().map(PurchaseDto::of).toList());

        return data;
    }

    public Map<String, Object> findAllInStorePurchase(int pageNum, String dateForm){

        Map<String, Object> result = new HashMap<>();
        result.put("totalReqSum", purchaseRepository.getTotalInStockSumByDate(dateForm));
        result.put("inStoreList", purchaseRepository.getTotalInStockListByDate(PageRequest.of(pageNum, 10), dateForm)
                .stream().map(PurchaseDto::of).toList());

        return result;
    }

    public List<PurchaseDto> findAllUnconfirmedPurchase(int pageNum){
        return purchaseRepository.findAllByApprovedStatus(RequestStatus.UNCONFIRMED, PageRequest.of(pageNum, 10))
                .stream().map(PurchaseDto::of).toList();
    }

    public List<PurchaseDto> findAllConfirmedPurchase(int pageNum){
        return purchaseRepository.findAllByApprovedStatusNot(RequestStatus.UNCONFIRMED, PageRequest.of(pageNum, 10))
                .stream().map(PurchaseDto::of).toList();
    }


    public void reqPurchase(PurchaseDto purchaseDto){
        purchaseRepository.save(PurchaseDto.toNewEntity(purchaseDto));
    }

    public void reqPurchase(PurchaseEntity purchaseEntity){purchaseRepository.save(purchaseEntity);}
}
