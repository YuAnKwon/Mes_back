package com.mes_back.service;

import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemImg;
import com.mes_back.repository.OrderItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemImgService {

    @Value("${imgLocation}")
    private String imgLocation;
    final OrderItemImgRepository OrderItemImgRepository;


    // 이미지 저장 메서드
    public void saveImages(OrderItem orderItem, List<MultipartFile> imgFiles) {
        if (imgFiles == null || imgFiles.isEmpty()) return;

        for (MultipartFile file : imgFiles) {
            String oriName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + oriName;

            // 1️⃣ 폴더 존재 여부 확인 및 생성
            File dir = new File(imgLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2️⃣ 파일 저장
            File saveFile = new File(dir, savedName);//실제 저장 경로
            try {
                file.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            // 3️⃣ DB 저장
            OrderItemImg img = new OrderItemImg();
            img.setOrderItem(orderItem);
            img.setImgOriName(oriName);
            img.setImgFileName(savedName);
            img.setImgUrl("/img/" + savedName);

            OrderItemImgRepository.save(img);
        }
    }

}
