package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.user.ClientUserDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.rating.BusinessRating;
import bookit.backend.model.entity.rating.Rating;
import bookit.backend.model.entity.rating.WorkerRating;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.request.CreateRatingRequest;
import bookit.backend.repository.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BusinessService businessService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public HttpStatus addBusinessRating(long businessId, long clientId, CreateRatingRequest request) {
        Optional<BusinessDto> businessOptional = businessService.getBusiness(businessId);
        if(businessOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Business business = modelMapper.map(businessOptional.get(), Business.class);

        Optional<ClientUserDto> clientOptional = userService.getClientUserById(clientId);
        if(clientOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        ClientUser client = modelMapper.map(clientOptional.get(), ClientUser.class);

        BusinessRating businessRating = BusinessRating.builder()
                .grade(request.getGrade())
                .business(business)
                .client(client)
                .comment(request.getComment())
                .build();
        ratingRepository.save(businessRating);
        return HttpStatus.CREATED;
    }

    public HttpStatus addWorkerRating(long workerId, long clientId, CreateRatingRequest request) {
        Optional<WorkerUserDto> workerOptional = userService.getWorkerUserById(workerId);
        if(workerOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        WorkerUser worker = modelMapper.map(workerOptional.get(), WorkerUser.class);

        Optional<ClientUserDto> clientOptional = userService.getClientUserById(clientId);
        if(clientOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        ClientUser client = modelMapper.map(clientOptional.get(), ClientUser.class);

        WorkerRating workerRating = WorkerRating.builder()
                .grade(request.getGrade())
                .worker(worker)
                .client(client)
                .comment(request.getComment())
                .build();
        ratingRepository.save(workerRating);
        return HttpStatus.CREATED;
    }

    public HttpStatus updateRating(long ratingId, long clientId, CreateRatingRequest request) {
        var ratingOptional = ratingRepository.findById(ratingId);
        if(ratingOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Rating rating = ratingOptional.get();
        if(rating.getClient().getId() != clientId) {
            return HttpStatus.FORBIDDEN;
        }

        rating.setGrade(request.getGrade());
        rating.setComment(request.getComment());
        ratingRepository.save(rating);
        return HttpStatus.OK;
    }

    public HttpStatus deleteRating(long ratingId, long clientId) {
        var ratingOptional = ratingRepository.findById(ratingId);
        if(ratingOptional.isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Rating rating = ratingOptional.get();
        if(rating.getClient().getId() != clientId) {
            return HttpStatus.FORBIDDEN;
        }

        ratingRepository.delete(rating);
        return HttpStatus.OK;
    }
}
