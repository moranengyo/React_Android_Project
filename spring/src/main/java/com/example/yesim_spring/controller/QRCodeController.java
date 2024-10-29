package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.Dto.UsageDto;
import com.example.yesim_spring.service.QRCodeService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qrCodeService;

    @PostMapping("/manager/qrcode")
    public ResponseEntity<byte[]> makeQRCode(@RequestParam String itemId, @RequestParam String purchaseId) {
        try {
            byte[] qrImage = qrCodeService.makeQRcode(itemId, purchaseId, 300, 300);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"");

            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/user/scanQRCode")
    public ResponseEntity<Map<String, Object>> scanQRCode(@RequestParam Long itemId, @RequestParam Long purchaseId) {
        // 아이템 정보
        // 입고 정보
        return ResponseEntity.ok(qrCodeService.fetchItemWithPurchase(itemId, purchaseId));
    }

    @PostMapping("/user/useItem")
    public ResponseEntity<Boolean> useItem(@RequestBody UsageDto usageDto) {
        // 총수량 차감
        // 사용이력 save
        try {
            boolean result = qrCodeService.updateCountAndSaveUsage(usageDto);

            if (result) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/manager/inStock")
    public ResponseEntity<Boolean> inStock(@RequestParam Long itemId, @RequestParam Long purchaseId) {
        // 총수량 증가
        // 구매 status 변경
        // 구매 approve time 업데이트
        try {
            qrCodeService.updateCountAndStatusAndTime(itemId, purchaseId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
