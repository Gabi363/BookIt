package bookit.backend.service;

import bookit.backend.model.dto.UserPrizeDto;
import bookit.backend.model.entity.UserPrize;
import bookit.backend.repository.UserPrizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class DiscountService {

    private final UserPrizeRepository userPrizeRepository;
    private final ModelMapper modelMapper;

    public List<UserPrizeDto> getUserPrizes(long userId) {
        return userPrizeRepository.getUserPrizesByUser_Id(userId)
                .stream()
                .map(userPrize -> modelMapper.map(userPrize, UserPrizeDto.class))
                .toList();
    }

    public boolean checkDiscountCode(String discountCode) {
        List<UserPrize> userPrizes = userPrizeRepository.getUserPrizesByDiscountCode(discountCode);
        if(userPrizes.isEmpty()) return false;
        UserPrize userPrize = userPrizes.get(0);
        userPrize.setUsed(true);
        userPrizeRepository.saveAndFlush(userPrize);
        return true;
    }

}
