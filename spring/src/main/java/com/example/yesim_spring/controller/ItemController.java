package com.example.yesim_spring.controller;

import com.example.yesim_spring.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.yesim_spring.util.Const.*;


@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/user/item/detail/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        try {
            return ResponseEntity.ok(itemService.findById(itemId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/item/{pageNum}")
    public ResponseEntity<?> getItemList(@PathVariable int pageNum) {
        return ResponseEntity.ok(itemService.findAllItem(pageNum));
    }

    @GetMapping("/manager/item/{pageNum}")
    public ResponseEntity<?> getItemListAndCnt(@PathVariable int pageNum) {
        return ResponseEntity.ok(itemService.findAllItemAndCnt(pageNum));
    }

    @GetMapping("/user/item/search")
    public ResponseEntity<?> getItemListByNameSearch(@RequestParam(name = "searchVal") String searchVal) {
        return ResponseEntity.ok(itemService.findAllByNameContaining(searchVal));
    }

    @GetMapping("/user/item/search/p/{pageNum}")
    public ResponseEntity<?> getItemListByPageSearch(@PathVariable int pageNum, @RequestParam(name = "searchVal") String searchVal) {
        return ResponseEntity.ok(itemService.findAllByNameContaining(pageNum, searchVal));
    }

    @GetMapping("/manager/item/under-min/{pageNum}")
    public ResponseEntity<?> getItemUnderMin(@PathVariable int pageNum) {
        return ResponseEntity.ok(
                itemService.getItemUnderMin(pageNum)
        );
    }

    @GetMapping("/manager/inout/{pageNum}")
    public ResponseEntity<?> getItemInout(@PathVariable int pageNum,
                                          @RequestParam String date
    ) {

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

    @Value("${spring.web.resources.static-locations}")
    private String uploadPath;

    @Value("${image-base-url}")
    private String imageBaseUrl;

    @PostMapping("/manager/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String originalFilename = file.getOriginalFilename();
            String newFilename = timestamp + "_" + originalFilename;

            String cleanedUploadPath = uploadPath.replace("file:///", "");
            Path filePath = Paths.get(cleanedUploadPath, newFilename);

            Files.write(filePath, file.getBytes());
            String fileUrl = newFilename;

            return ResponseEntity.ok().body("{\"thumbnail\":\"" + fileUrl + "\"}");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드 실패");
        }
    }
}
