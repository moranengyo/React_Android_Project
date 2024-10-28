package com.example.yesim_spring.service;

import com.example.yesim_spring.database.entity.define.RequestStatus;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashBoardService {


    private final PurchaseRepository purchaseRepository;
    private final UsageRepository usageRepository;

    public Map<String, Object> dashBoardInoutCnt(String dateForm){
        Map<String, Object> data = new HashMap<>();
        data.put("inStoreCnt", purchaseRepository.getTotalStockCountByDate(dateForm));
        data.put("usageCnt", usageRepository.getTotalUsageCntByDate(dateForm));

        return data;
    }


    public Map<String, Object> dashBoardPurchaseCnt(){
        Map<String, Object> data = new HashMap<>();

        data.put("requestCnt", purchaseRepository.countAllByApprovedStatus(RequestStatus.WAIT));
        data.put("approvedCnt", purchaseRepository.countAllByApprovedStatus(RequestStatus.APPROVE));
        data.put("canceledCnt", purchaseRepository.countAllByApprovedStatus(RequestStatus.CANCEL));

        return data;
    }
}
