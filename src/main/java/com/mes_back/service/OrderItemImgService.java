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
    //이미지 파일들을 실제로 파일시스템에 저장하고 관련 메타(?)를 DB에 남김.
    public void saveImages(OrderItem orderItem, List<MultipartFile> imgFiles) {
        //파일이 없으면 바로 종료(불필요한 처리 방지).왜 null, empty로 다르지
        if (imgFiles == null || imgFiles.isEmpty()) return;

        for (MultipartFile file : imgFiles) {
            String oriName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + oriName;

            // 1️⃣ 폴더 존재 여부 확인 및 생성
            // 이미지 저장용 루트 디렉토리(File 객체) 생성
            File dir = new File(imgLocation);
            // 디렉토리가 없으면 필요한 모든 상위 폴더까지 만들어 줌.
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 2️⃣ 파일 저장
            // 실제 저장될 파일 경로(File 객체) 준비.
            File saveFile = new File(dir, savedName);
            try {
                //MultipartFile을 물리 파일로 디스크에 기록;
                file.transferTo(saveFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            // 3️⃣ DB 저장
            OrderItemImg img = new OrderItemImg();
            img.setOrderItem(orderItem);
            img.setImgOriName(oriName);
            img.setImgFileName(savedName);
            img.setImgUrl("/img/" + savedName);//클라이언트가 접근할 URL(또는 리소스 핸들러 매핑 경로) 설정.

            OrderItemImgRepository.save(img);
        }
    }

}
