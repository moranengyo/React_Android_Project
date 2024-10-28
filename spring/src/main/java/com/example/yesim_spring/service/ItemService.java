package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.ItemDto;
import com.example.yesim_spring.database.Dto.ItemInOutDto;
import com.example.yesim_spring.database.repository.ItemRepository;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final UsageRepository usageRepository;

    public ItemDto findById(Long id) {

        var item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found "));

        return ItemDto.of(item);
    }

    public List<ItemDto> findAllItem(int pageNum){
        return itemRepository.findAll(PageRequest.of(pageNum, 10))
                .stream().map(ItemDto::of).toList();
    }

    public Map<String, Object> findAllItemAndCnt(int pageNum) {
        Map<String, Object> data = new HashMap<>();

        data.put("itemTotalCnt", itemRepository.count());
        data.put("itemList", itemRepository.findAll(PageRequest.of(pageNum, 10))
                .stream().map(ItemDto::of).toList());

        return data;
    }

    public List<ItemDto> findAllByNameContaining(String searchVal){
        return itemRepository.findAllByNameContaining(searchVal)
                .stream().map(ItemDto::of).toList();
    }


    public Map<String, Object> findAllByNameContaining(int pageNum, String searchVal){
        Map<String, Object> data = new HashMap<>();

        data.put("itemTotalCnt", itemRepository.countAllByNameContaining(searchVal));
        data.put("searchResult",itemRepository.findAllByNameContaining(searchVal, PageRequest.of(pageNum, 10))
                .stream().map(ItemDto::of).toList());

        return data;
    }

    public Map<String, Object> getItemUnderMin(int pageNum){
        Map<String, Object> data = new HashMap<>();

        data.put("allUnderMinCnt", itemRepository.countAllUnderMin());
        data.put("itemList", itemRepository.getItemUnderMin(PageRequest.of(pageNum, 10))
                .stream().map(ItemDto::of).toList());
        return data;
    }

    public Map<String, Object> getItemInOutList(int pageNum, String dateForm){

        Map<String, Object> result = new HashMap<>();
        result.put("inoutList", itemRepository.getItemInOut(PageRequest.of(pageNum,10), dateForm).stream().map(ItemInOutDto::of).toList());
        result.put("totalReqSum", purchaseRepository.getTotalInStockSumByDate(dateForm));
        result.put("totalUsageSum", usageRepository.getTotalUsageSumByDate(dateForm));

        return result;
    }


}
