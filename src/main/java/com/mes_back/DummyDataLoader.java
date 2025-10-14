package com.mes_back;

import com.mes_back.constant.*;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.entity.MaterialIn;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.CompanyRepository;
import com.mes_back.repository.MaterialInRepository;
import com.mes_back.repository.MaterialRepository;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final OrderItemRepository orderItemRepository;
    private final CompanyRepository companyRepository;
    private final MaterialRepository materialRepository;
    private final MaterialInRepository materialInRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1️⃣ 회사 더미 생성
        List<Company> companies = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Company.builder()
                        .companyType(CompanyType.CLIENT)
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

        // Material 더미
        List<Material> materials = List.of(
                Material.builder().name("ABS 수지").code("MAT-001").color("백색").spec(25).scale("kg").manufacturer("LG화학").remark("고강도용").type(MaterialType.HARDENER).company(companies.get(0)).useYn(Yn.Y).build(),
                Material.builder().name("PP 원료").code("MAT-002").color("투명").spec(30).scale("kg").manufacturer("롯데케미칼").remark("내열성 우수").type(MaterialType.HARDENER).company(companies.get(1)).useYn(Yn.Y).build(),
                Material.builder().name("폴리카보네이트").code("MAT-003").color("투명").spec(20).scale("kg").manufacturer("한화솔루션").remark("고투명성").type(MaterialType.CLEANER).company(companies.get(2)).useYn(Yn.Y).build(),
                Material.builder().name("알루미늄 판재").code("MAT-004").color("은색").spec(10).scale("mm").manufacturer("조선금속").remark("경량화 소재").type(MaterialType.THINNER).company(companies.get(3)).useYn(Yn.Y).build(),
                Material.builder().name("스테인리스 봉").code("MAT-005").color("은회색").spec(15).scale("mm").manufacturer("포스코").remark("내식성 우수").type(MaterialType.HARDENER).company(companies.get(4)).useYn(Yn.Y).build(),
                Material.builder().name("구리 와이어").code("MAT-006").color("적색").spec(5).scale("mm").manufacturer("LS전선").remark("전도율 우수").type(MaterialType.PAINT).company(companies.get(5)).useYn(Yn.Y).build(),
                Material.builder().name("PVC 파이프").code("MAT-007").color("회색").spec(50).scale("mm").manufacturer("대한유화").remark("내구성 우수").type(MaterialType.THINNER).company(companies.get(6)).useYn(Yn.Y).build(),
                Material.builder().name("고무 시트").code("MAT-008").color("검정").spec(3).scale("mm").manufacturer("한국고무").remark("충격 흡수용").type(MaterialType.CLEANER).company(companies.get(7)).useYn(Yn.Y).build(),
                Material.builder().name("카본 섬유").code("MAT-009").color("흑색").spec(1).scale("mm").manufacturer("도레이코리아").remark("초경량 소재").type(MaterialType.PAINT).company(companies.get(8)).useYn(Yn.Y).build(),
                Material.builder().name("폴리우레탄 폼").code("MAT-010").color("베이지").spec(100).scale("mm").manufacturer("한화첨단소재").remark("단열용").type(MaterialType.HARDENER).company(companies.get(9)).useYn(Yn.Y).build()
        );
        materialRepository.saveAll(materials);

        // 2️⃣ OrderItem 더미 생성
        OrderItemType[] types = OrderItemType.values();
        CoatingMethod[] coatings = CoatingMethod.values();

        IntStream.rangeClosed(1, 100).forEach(i -> {
            OrderItem item = OrderItem.builder()
                    .company(companies.get(i % companies.size()))
                    .itemName("품목" + i)
                    .itemCode(String.valueOf(100 + i))
                    .type(types[i % types.length])       // ⚡ Enum 그대로 저장
                    .unitPrice(1000 + i * 10)
                    .color("색상" + ((i % 5) + 1))
                    .coatingMethod(coatings[i % coatings.length])
                    .remark("비고 " + i + "123123")
                    .useYn(Yn.Y)
                    .build();

            orderItemRepository.save(item);
        });

        // 원자재 입고 더미
        Material material = materialRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        MaterialIn materialIn = MaterialIn.builder()
                .material(material)
                .manufactureDate(LocalDate.of(2025, 10, 10))  // 제조일자
                .inAmount(500)                               // 입고수량
                .inNum("IN-20251010-001")                    // 입고번호
                .inDate("2025-10-10")                        // 입고일자
                .delYn(Yn.N)                                 // 삭제 여부
                .build();
        materialInRepository.save(materialIn);

        System.out.println("더미 Company 10개 + OrderItem 100개 삽입 완료!");
    }
}
