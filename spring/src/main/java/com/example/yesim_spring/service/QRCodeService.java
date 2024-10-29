package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.ItemDto;
import com.example.yesim_spring.database.Dto.ItemInOutDto;
import com.example.yesim_spring.database.Dto.PurchaseDto;
import com.example.yesim_spring.database.Dto.UsageDto;
import com.example.yesim_spring.database.entity.define.RequestStatus;
import com.example.yesim_spring.database.repository.ItemRepository;
import com.example.yesim_spring.database.repository.PurchaseRepository;
import com.example.yesim_spring.database.repository.UsageRepository;
import com.example.yesim_spring.database.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hibernate.mapping.Any;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final UsageRepository usageRepository;
    private final UserRepository userRepository;

    public byte[] makeQRcode(String itemId, String purchaseId, int width, int height) throws WriterException, IOException {
        String combinedString = itemId + "/" + purchaseId;

        BitMatrix bitMatrix = new MultiFormatWriter().encode(combinedString, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Map<String, Object> fetchItemWithPurchase(Long itemId, Long purchaseId) {
        Map<String, Object> result = new HashMap<>();

        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("not found item"));

        var purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("not found purchase"));

        result.put("item", ItemDto.of(item));
        result.put("purchase", PurchaseDto.of(purchase));

        return result;
    }

    @Transactional
    public Boolean updateCountAndSaveUsage(UsageDto dto) {
        var user = userRepository.findByUserId(dto.user().userId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        var item = itemRepository.findById(dto.item().id())
                .orElseThrow(() -> new RuntimeException("Item Not Found"));

        if (!item.useItem(dto.usageNum())) {
            return false;
        }

        itemRepository.save(item);
        usageRepository.save(UsageDto.toNewEntity(dto, user, item));
        return true;
    }

    public void updateCountAndStatusAndTime(Long itemId, Long purchaseId) {
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item Not Found"));

        var purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase Not Found"));

        if (purchase.getItem().getId() == itemId) {
            item.inStock(purchase.getReqNum());
            itemRepository.save(item);

            purchase.updateStatusAndTime(RequestStatus.IN_STOCK);
            purchaseRepository.save(purchase);
        }

    }
}
