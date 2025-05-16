package bookit.backend.service;

import bookit.backend.model.dto.PrizeDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.points.BusinessPoints;
import bookit.backend.model.entity.points.ClientPoints;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.enums.Points;
import bookit.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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
            checkClientPoints(clientPoints.getPointsNumber());
            return;
        }
        ClientPoints clientPoints = ClientPoints.builder()
                .updateDate(LocalDate.now())
                .pointsNumber(Points.CLIENT_POINTS.getValue())
                .client(client.get())
                .build();
        loyalPointsRepository.save(clientPoints);
        checkClientPoints(clientPoints.getPointsNumber());
    }

    private void checkClientPoints(int pointsNumber) {
        PrizeDto prizeDto = prizeService.getPrizes()
                .stream()
                .filter(prize -> prize.getPointsThreshold() <= pointsNumber)
                .min((p1, p2) -> p1.getPointsThreshold().compareTo(p2.getPointsThreshold()))
                .orElse(null);

        if(prizeDto == null) return;
        log.info("Prize found: {}", prizeDto);

    }

    private void addBusinessPoints(long businessId) throws Exception {
        Optional<Business> businessOptional = businessRepository.findById(businessId);
        if (businessOptional.isEmpty()) throw new Exception();

        Optional<BusinessPoints> businessPointsOptional = businessPointsRepository.getFirstByBusinessId(businessId);
        if(businessPointsOptional.isPresent()) {
            BusinessPoints businessPoints = businessPointsOptional.get();
            businessPoints.setPointsNumber(businessPoints.getPointsNumber() + Points.BUSINESS_POINTS.getValue());
            loyalPointsRepository.save(businessPoints);
            checkClientPoints(businessPoints.getPointsNumber());
            return;
        }
        BusinessPoints businessPoints = BusinessPoints.builder()
                .updateDate(LocalDate.now())
                .pointsNumber(Points.BUSINESS_POINTS.getValue())
                .business(businessOptional.get())
                .build();
        loyalPointsRepository.save(businessPoints);
        checkClientPoints(businessPoints.getPointsNumber());
    }
}
