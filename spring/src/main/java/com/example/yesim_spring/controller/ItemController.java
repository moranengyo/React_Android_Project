package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.Dto.ItemDto;
import com.example.yesim_spring.service.ItemService;
import com.example.yesim_spring.util.ImageURL;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.yesim_spring.util.Const.*;


@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/user/item/detail/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        try{
            itemService.findById(itemId);
            return ResponseEntity.ok(itemService.findById(itemId));
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/item/{pageNum}")
    public ResponseEntity<?> getItemList(@PathVariable int pageNum){
        return ResponseEntity.ok(itemService.findAllItem(pageNum));
    }

    @GetMapping("/manager/item/{pageNum}")
    public ResponseEntity<?> getItemListAndCnt(@PathVariable int pageNum){
        return ResponseEntity.ok(itemService.findAllItemAndCnt(pageNum));
    }

    @GetMapping("/user/item/search")
    public ResponseEntity<?> getItemListByNameSearch(@RequestParam(name = "searchVal") String searchVal){
        return ResponseEntity.ok(itemService.findAllByNameContaining(searchVal));
    }

    @GetMapping("/user/item/search/p/{pageNum}")
    public ResponseEntity<?> getItemListByPageSearch(@PathVariable int pageNum, @RequestParam(name = "searchVal") String searchVal){
        return ResponseEntity.ok(itemService.findAllByNameContaining(pageNum, searchVal));
    }

    @GetMapping("/manager/item/under-min/{pageNum}")
    public ResponseEntity<?> getItemUnderMin(@PathVariable int pageNum){
        return ResponseEntity.ok(
                itemService.getItemUnderMin(pageNum)
        );
    }

    @GetMapping("/s-manager/inout/{pageNum}")
    public ResponseEntity<?> getItemInout(@PathVariable int pageNum,
                                          @RequestParam String date
    ){

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


        return ResponseEntity.ok(itemService.getItemInOutList(pageNum, dateForm));
    }

}
