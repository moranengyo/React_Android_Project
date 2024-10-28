package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.ItemInOutDto;
import com.example.yesim_spring.database.Dto.PurchaseDto;
import com.example.yesim_spring.database.Dto.UsageDto;
import com.example.yesim_spring.database.entity.PurchaseEntity;
import com.example.yesim_spring.database.entity.define.RequestStatus;
import com.example.yesim_spring.database.repository.ItemRepository;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import com.example.yesim_spring.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsageService {
    private final UsageRepository usageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;

    public  Map<String, Object> getUsageSearchList(int pageNum, String dateForm, String searchVal){
        Map<String, Object> data = new HashMap<>();

        data.put("searchResultTotalUsageNum", usageRepository.getSearchUsageNumSumByDate(dateForm, searchVal));
        data.put("searchResultTotalUsage", usageRepository.getSearchUsageCountByDate(dateForm, searchVal));
        data.put("searchResultTotalPay", usageRepository.getSearchUsagePaySumByDate(dateForm, searchVal));
        data.put("searchResult", usageRepository.getSearchUsageList(dateForm, searchVal, PageRequest.of(pageNum, 10))
                .stream().map(UsageDto::of).toList());

        return data;
    }

    public Map<String, Object> getTotalUsageListByDate(int pageNum, String dateForm){
        Map<String, Object> result = new HashMap<>();

        result.put("totalUsageSum", usageRepository.getTotalUsageSumByDate(dateForm));
        result.put("usageList", usageRepository.getTotalUsageListByDate(PageRequest.of(pageNum, 10), dateForm)
                .stream().map(UsageDto::of).toList());

        return result;

    }

    public Map<String, Object> getTotalUsageListByDateAndGroupedItem(int pageNum, String dateForm){
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsageSum", usageRepository.getTotalUsageSumByDate(dateForm));
        result.put("usageList", usageRepository.getTotalUsageListByDateAndGroupedItem(PageRequest.of(pageNum, 10), dateForm)
                .stream().map(UsageDto::of).toList());

        return result;
    }

    public Map<String, Object> getTotalUsageListByDateAndUser(int pageNum, String dateForm, String userId){
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsageSum", usageRepository.getTotalUsageSumByDateAndUser(dateForm, userId));
        result.put("usageList", usageRepository.getTotalUsageListByDateAndUser(PageRequest.of(pageNum, 10), dateForm, userId)
                .stream().map(UsageDto::of).toList());

        return result;
    }

    public Boolean usageItem(UsageDto dto){
        var user = userRepository.findByUserId(dto.user().userId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        var item = itemRepository.findById(dto.item().id())
                        .orElseThrow(() -> new RuntimeException("Item Not Found"));

        if(!item.useItem(dto.usageNum())) {
            return false;
        }


        itemRepository.save(item);
        usageRepository.save(UsageDto.toNewEntity(dto, user, item));

        if(item.getTotalNum() >= item.getMinNum()){
            return true;
        }

        if(purchaseRepository.countAllByItem_Id(item.getId()) > 0){
            return true;
        }

            var purchase = PurchaseEntity.builder()
                    .item(item)
                    .title("구매 요청 : " + item.getName())
                    .reqNum(item.getMinNum())
                    .reqTime(LocalDateTime.now())
                    .approvedStatus(RequestStatus.UNCONFIRMED)
                    .approvalComment("적정 수량 부족으로 인한 요청입니다.")
                    .user(user)
                    .newYn("N")
                    .build();

            purchaseRepository.save(purchase);
        return true;
    }
}
