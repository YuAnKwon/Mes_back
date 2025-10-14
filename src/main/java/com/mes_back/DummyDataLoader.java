package com.mes_back;

import com.mes_back.constant.*;
import com.mes_back.entity.Company;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.CompanyRepository;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final OrderItemRepository orderItemRepository;
    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1️⃣ 회사 더미 생성
        List<Company> companies = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Company.builder()
                        .companyType(CompanyType.CLIENT)  // 임의 타입
                        .companyName("회사" + i)
                        .ceoName("대표" + i)
                        .ceoPhone("010-0000-000" + i)
                        .businessNum("123-45-000" + i)
                        .zipcode("12345")
                        .addressBase("주소기본" + i)
                        .addressDetail("주소상세" + i)
                        .businessYn(Yn.Y)
                        .build())
                .toList();

        companyRepository.saveAll(companies);

        // 2️⃣ OrderItem 더미 생성
        OrderItemType[] types = OrderItemType.values();
        CoatingMethod[] coatings = CoatingMethod.values();

        IntStream.rangeClosed(1, 100).forEach(i -> {
            OrderItem item = OrderItem.builder()
                    .company(companies.get(i % companies.size())) // 순환해서 회사 연결
                    .itemName("품목" + i)
                    .itemCode(String.valueOf(100 + i))
                    .type(types[i % types.length])
                    .unitPrice(1000 + i * 10)
                    .color("색상" + ((i % 5) + 1))
                    .coatingMethod(coatings[i % coatings.length])
                    .remark("비고 " + i + "123123")
                    .useYn(Yn.Y)
                    .build();

            orderItemRepository.save(item);
        });

        System.out.println("더미 Company 10개 + OrderItem 100개 삽입 완료!");
    }
}
