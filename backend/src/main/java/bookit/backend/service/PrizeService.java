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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public PrizeDto getMaxPrize(int pointsNumber) {
        return getPrizes().stream()
                .filter(prize -> prize.getPointsThreshold() <= pointsNumber)
                .max(Comparator.comparing(PrizeDto::getPointsThreshold))
                .orElse(null);
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

    public HttpStatus deletePrize(Long id){
        prizeRepository.deleteById(id);
        return HttpStatus.OK;
    }

    public HttpStatus editPrize(Long id, AddPrizeRequest request){
        Optional<Prize> prizeOptional = prizeRepository.findById(id);
        if(prizeOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        Prize prize = prizeOptional.get();
        prize.setPrizeName(request.getPrizeName());
        prize.setDescription(request.getDescription());
        prize.setPrice(request.getPrice());
        prize.setPointsThreshold(request.getPointsThreshold());

        prizeRepository.save(prize);
        return HttpStatus.OK;
    }
}
