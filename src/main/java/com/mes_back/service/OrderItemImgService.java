package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.OrderItemImgDto;
import com.mes_back.entity.Company;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemImg;
import com.mes_back.repository.OrderItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemImgService {

    @Value("${imgLocation}")
    private String imgLocation;
    final OrderItemImgRepository orderItemImgRepository;


    // 이미지 저장 메서드
    //이미지 파일들을 실제로 파일시스템에 저장하고 관련 메타(?)를 DB에 남김.
    @Transactional
    public void saveImages(OrderItem orderItem, List<OrderItemImgDto> imageDtos, List<MultipartFile> imgFiles) {

        List<OrderItemImgDto> imageDtosSafe = imageDtos != null ? imageDtos : List.of();

        // 1️⃣ DB에서 기존 이미지 조회
        List<OrderItemImg> existingImgs = orderItemImgRepository.findByOrderItem(orderItem);

        // 2️⃣ 프론트에서 보낸 ID 목록
        List<Long> incomingIds = imageDtosSafe.stream()
                .map(OrderItemImgDto::getId)
                .filter(Objects::nonNull) // null이면 삭제되지 않도록
                .toList();

        // 3️⃣ DB에 있는 이미지 중 프론트에 없는 것은 삭제
        for (OrderItemImg img : existingImgs) {
            if (!incomingIds.contains(img.getId())) {
                orderItemImgRepository.delete(img);
            }
        }


        List<OrderItemImg> finalImgs = new ArrayList<>();

        // 5️⃣ 새로 업로드된 이미지 처리
        if (imgFiles != null && !imgFiles.isEmpty()) {
            for (MultipartFile file : imgFiles) {
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

                OrderItemImg img = new OrderItemImg();
                img.setOrderItem(orderItem);
                img.setImgOriName(oriName);
                img.setImgFileName(savedName);
                img.setImgUrl("/img/" + savedName);
                img.setRepYn(Yn.N); // 새 이미지는 기본값 N
                finalImgs.add(img);
                System.out.println("==================================================================");
                System.out.println(img.getImgFileName());
                System.out.println("새 이미지 repYn" + img.getRepYn());
            }
        }

        // 4️⃣ 기존 이미지 DTO에서 repYn는 무시하고 모두 N으로 초기화
        for (OrderItemImgDto dto : imageDtosSafe) {
            if (dto.getId() != null) {
                OrderItemImg img = orderItemImgRepository.findById(dto.getId()).orElseThrow();
                img.setRepYn(dto.getRepYn()); // 무조건 N으로 초기화
                System.out.println("dto의 repYn이 들어가는지" + img.getRepYn());
                finalImgs.add(img);
            }
        }



        // 6️⃣ 전체 이미지 중 첫 번째 이미지를 Y로 강제 설정
        boolean hasY = finalImgs.stream().anyMatch(img -> Yn.Y.equals(img.getRepYn()));
        if (!hasY && !finalImgs.isEmpty()) {
            // 없으면 새 이미지든 기존 이미지든 맨 앞 놈을 대표로
            finalImgs.get(0).setRepYn(Yn.Y);
            System.out.println("==================================================================");
            System.out.println(finalImgs.get(0).getRepYn());
            System.out.println("==================================================================");
        }
        // 7️⃣ 한 번에 DB 저장
        orderItemImgRepository.saveAll(finalImgs);
        for (OrderItemImg img : finalImgs) {
            System.out.println("==================================================================");
            System.out.println(img.getImgFileName());
            System.out.println(img.getRepYn());
        }
    }

}
