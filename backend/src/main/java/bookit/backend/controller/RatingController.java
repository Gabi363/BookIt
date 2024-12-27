package bookit.backend.controller;

import bookit.backend.model.request.CreateRatingRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("rating")
public class RatingController {

    private final RatingService ratingService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @PostMapping("/business/{businessId}")
    @Operation(summary = "Add business rating")
    public ResponseEntity<?> addBusinessRating(@Valid @RequestBody CreateRatingRequest request,
                                               @PathVariable long businessId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(!userInfo.isClient()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = ratingService.addBusinessRating(businessId, userInfo.getId(), request);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/worker/{workerId}")
    @Operation(summary = "Add worker rating")
    public ResponseEntity<?> addWorkerRating(@Valid @RequestBody CreateRatingRequest request,
                                             @PathVariable long workerId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(!userInfo.isClient()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = ratingService.addWorkerRating(workerId, userInfo.getId(), request);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/update/{ratingId}")
    @Operation(summary = "Update rating")
    public ResponseEntity<?> updateRating(@Valid @RequestBody CreateRatingRequest request,
                                          @PathVariable long ratingId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();

        HttpStatus status = ratingService.updateRating(ratingId, userInfo.getId(), request);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{ratingId}")
    @Operation(summary = "Delete rating")
    public ResponseEntity<?> deleteRating(@PathVariable long ratingId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();

        HttpStatus status = ratingService.deleteRating(ratingId, userInfo.getId());
        return ResponseEntity.status(status).build();
    }
}