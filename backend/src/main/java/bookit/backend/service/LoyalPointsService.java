package bookit.backend.service;

import bookit.backend.model.dto.PrizeDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.Prize;
import bookit.backend.model.entity.UserPrize;
import bookit.backend.model.entity.points.BusinessPoints;
import bookit.backend.model.entity.points.ClientPoints;
import bookit.backend.model.entity.user.BusinessOwnerUser;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.User;
import bookit.backend.model.enums.Points;
import bookit.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class LoyalPointsService {

    private final LoyalPointsRepository loyalPointsRepository;
    private final BusinessRepository businessRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ClientPointsRepository clientPointsRepository;
    private final BusinessPointsRepository businessPointsRepository;
    private final PrizeService prizeService;
    private final UserPrizeRepository userPrizeRepository;

    public HttpStatus addLoyalPoints(long clientId, long businessId) {
        try {
            addClientPoints(clientId);
            addBusinessPoints(businessId);
        } catch (Exception e) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.CREATED;
    }

    private void addClientPoints(long clientId) throws Exception {
        Optional<ClientUser> client = userRepository.findById(clientId)
                .map(user -> modelMapper.map(user, ClientUser.class));
        if(client.isEmpty()) throw new Exception();

        Optional<ClientPoints> clientPointsOptional = clientPointsRepository.getFirstByClient_Id(clientId);
        if(clientPointsOptional.isPresent()) {
            ClientPoints clientPoints = clientPointsOptional.get();
            clientPoints.setPointsNumber(clientPoints.getPointsNumber() + Points.CLIENT_POINTS.getValue());
            loyalPointsRepository.save(clientPoints);
            User user = userRepository.findById(clientId).orElse(null);
            checkClientPoints(clientPoints.getPointsNumber(), user);
            return;
        }
        ClientPoints clientPoints = ClientPoints.builder()
                .updateDate(LocalDate.now())
                .pointsNumber(Points.CLIENT_POINTS.getValue())
                .client(client.get())
                .build();
        loyalPointsRepository.save(clientPoints);
        User user = userRepository.findById(clientId).orElse(null);
        checkClientPoints(clientPoints.getPointsNumber(), user);
    }

    private void checkClientPoints(int pointsNumber, User client) {
        PrizeDto prizeDto = prizeService.getMaxPrize(pointsNumber);
        if(prizeDto == null || client == null) return;

        UserPrize userPrize = userPrizeRepository.getUserPrizesByUser(client)
                .stream()
                .max(Comparator.comparing(up -> up.getPrize().getPointsThreshold()))
                .orElse(null);

        if(userPrize != null && Objects.equals(userPrize.getPrize().getId(), prizeDto.getId())) return;
        UserPrize newUserPrize = UserPrize.builder()
                .user(client)
                .prize(modelMapper.map(prizeDto, Prize.class))
                .used(false)
                .discountCode(generateDiscountCode())
                .build();
        userPrizeRepository.save(newUserPrize);
    }

    private void addBusinessPoints(long businessId) throws Exception {
        Optional<Business> businessOptional = businessRepository.findById(businessId);
        if (businessOptional.isEmpty()) throw new Exception();

        Optional<BusinessPoints> businessPointsOptional = businessPointsRepository.getFirstByBusinessId(businessId);
        if(businessPointsOptional.isPresent()) {
            BusinessPoints businessPoints = businessPointsOptional.get();
            businessPoints.setPointsNumber(businessPoints.getPointsNumber() + Points.BUSINESS_POINTS.getValue());
            loyalPointsRepository.save(businessPoints);
            checkBusinessPoints(businessPoints.getPointsNumber(), businessOptional.get().getOwner());
            return;
        }
        BusinessPoints businessPoints = BusinessPoints.builder()
                .updateDate(LocalDate.now())
                .pointsNumber(Points.BUSINESS_POINTS.getValue())
                .business(businessOptional.get())
                .build();
        loyalPointsRepository.save(businessPoints);
        checkBusinessPoints(businessPoints.getPointsNumber(), businessOptional.get().getOwner());
    }

    private void checkBusinessPoints(int pointsNumber, BusinessOwnerUser businessOwnerUser) {
        PrizeDto prizeDto = prizeService.getMaxPrize(pointsNumber);

        if(prizeDto == null || businessOwnerUser == null) return;
        UserPrize userPrize = userPrizeRepository.getUserPrizesByUser(businessOwnerUser)
                .stream()
                .max(Comparator.comparing(up -> up.getPrize().getPointsThreshold()))
                .orElse(null);

        if(userPrize != null && Objects.equals(userPrize.getPrize().getId(), prizeDto.getId())) return;
        UserPrize newUserPrize = UserPrize.builder()
                .user(businessOwnerUser)
                .prize(modelMapper.map(prizeDto, Prize.class))
                .used(false)
                .discountCode(generateDiscountCode())
                .build();
        userPrizeRepository.save(newUserPrize);
    }

    private String generateDiscountCode() {
        String generatedString;
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        do {
            generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while(!userPrizeRepository.getUserPrizesByDiscountCode(generatedString).isEmpty());
        return generatedString;
    }
}
