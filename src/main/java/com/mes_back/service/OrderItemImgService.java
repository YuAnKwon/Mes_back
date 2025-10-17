package com.mes_back.service;

import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemImg;
import com.mes_back.repository.OrderItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemImgService {

    final OrderItemImgRepository OrderItemImgRepository;


    // 이미지 저장 메서드
    public void saveImages(OrderItem orderItem, List<MultipartFile> imgFiles) {
        if (imgFiles == null || imgFiles.isEmpty()) return;

        for (MultipartFile file : imgFiles) {
            String oriName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + oriName;
            String savePath = "C:/upload/orderitem/" + savedName;

            try {
                file.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            OrderItemImg img = new OrderItemImg();
            img.setOrderItem(orderItem);
            img.setImgOriName(oriName);
            img.setImgFileName(savedName);
            img.setImgUrl("/uploads/orderitem/" + savedName);

            OrderItemImgRepository.save(img);
        }
    }

}
