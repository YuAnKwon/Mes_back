package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.RoutingDto;
import com.mes_back.entity.Routing;
import com.mes_back.repository.RoutingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoutingService {
    private final RoutingRepository routingRepository;
    public List<RoutingDto> getRoutingList() {
        // 삭제여부 N인것만 조회
        List<Routing> routingList = routingRepository.findByDelYn(Yn.N);
        List<RoutingDto> dtoList = new ArrayList<>();

        for(Routing routing : routingList){
           RoutingDto dto = RoutingDto.builder()
                   .id(routing.getId())
                   .processCode(routing.getProcessCode())
                   .processName(routing.getProcessName())
                   .processTime(routing.getProcessTime())
                   .remark(routing.getRemark())
                   .build();

           dtoList.add(dto);
        }
        return dtoList;
    }

    public void newRouting(List<RoutingDto> routingDto) {
        // 공정코드 갖고오기

        for (int i = 0; i < routingDto.size(); i++) {
            boolean exists = routingRepository.existsByProcessCodeAndDelYn(
                    routingDto.get(i).getProcessCode(),
                    Yn.N
            );
            if(exists){
                throw new IllegalArgumentException("이미 존재하는 공정코드입니다.");
            }
            Routing routing = Routing.builder()
                    .processCode(routingDto.get(i).getProcessCode())
                    .processName(routingDto.get(i).getProcessName())
                    .processTime(routingDto.get(i).getProcessTime())
                    .remark(routingDto.get(i).getRemark())
                    .delYn(Yn.N)
                    .build();
            routingRepository.save(routing);
        }
    }

    public void deleteRouting(Long id) {
        Routing routing = routingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        routing.setDelYn(Yn.Y);
        routingRepository.save(routing);
    }
}
