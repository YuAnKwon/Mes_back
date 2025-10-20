package com.mes_back.service;

import com.mes_back.constant.Yn;
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
    //이미지 파일들을 실제로 파일시스템에 저장하고 관련 메타(?)를 DB에 남김.
    public void saveImages(OrderItem orderItem, List<MultipartFile> imgFiles) {
        //파일이 없으면 바로 종료(불필요한 처리 방지).왜 null, empty로 다르지
        if (imgFiles == null || imgFiles.isEmpty()) return;

        for (int i = 0; i < imgFiles.size(); i++) {
            MultipartFile file = imgFiles.get(i);
            String oriName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + oriName;

            File dir = new File(imgLocation);
            if (!dir.exists()) dir.mkdirs();

            File saveFile = new File(dir, savedName);
            try {
                file.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            // DB 저장
            OrderItemImg img = new OrderItemImg();
            img.setOrderItem(orderItem);
            img.setImgOriName(oriName);
            img.setImgFileName(savedName);
            img.setImgUrl("/img/" + savedName);

            // ✅ 맨 앞 이미지만 대표 이미지로
            img.setRepYn(i == 0 ? Yn.Y : Yn.N);

            OrderItemImgRepository.save(img);
        }
    }

}
