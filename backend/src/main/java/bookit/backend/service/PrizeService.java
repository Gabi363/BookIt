package bookit.backend.service;

import bookit.backend.model.dto.PrizeDto;
import bookit.backend.model.entity.Prize;
import bookit.backend.model.request.AddPrizeRequest;
import bookit.backend.repository.PrizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class PrizeService {

    private final PrizeRepository prizeRepository;
    private final ModelMapper modelMapper;

    public List<PrizeDto> getPrizes() {
        return prizeRepository.findAll()
                .stream()
                .map(prize -> modelMapper.map(prize, PrizeDto.class))
                .collect(Collectors.toList());
    }

    public HttpStatus addPrize(AddPrizeRequest request){
        Prize prize = Prize.builder()
                .prizeName(request.getPrizeName())
                .description(request.getDescription())
                .price(request.getPrice())
                .pointsThreshold(request.getPointsThreshold())
                .build();
        prizeRepository.save(prize);
        return HttpStatus.CREATED;
    }
}
