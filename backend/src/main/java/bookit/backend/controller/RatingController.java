package bookit.backend.controller;

import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.CreateRatingRequest;
import bookit.backend.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("rating")
public class RatingController {

    private final RatingService ratingService;
    private final ModelMapper modelMapper;

    @PostMapping("/business/{businessId}")
    @ManagedOperation(description = "Add business rating")
    public ResponseEntity<?> addBusinessRating(@Valid @RequestBody CreateRatingRequest request,
                                               @PathVariable long businessId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long clientId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.CLIENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = ratingService.addBusinessRating(businessId, clientId, request);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/worker/{workerId}")
    @ManagedOperation(description = "Add worker rating")
    public ResponseEntity<?> addWorkerRating(@Valid @RequestBody CreateRatingRequest request,
                                               @PathVariable long workerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long clientId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.CLIENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = ratingService.addWorkerRating(workerId, clientId, request);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/update/{ratingId}")
    @ManagedOperation(description = "Update rating")
    public ResponseEntity<?> updateRating(@Valid @RequestBody CreateRatingRequest request,
                                          @PathVariable long ratingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long clientId = modelMapper.map(auth.getPrincipal(), long.class);

        HttpStatus status = ratingService.updateRating(ratingId, clientId, request);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{ratingId}")
    @ManagedOperation(description = "Delete rating")
    public ResponseEntity<?> deleteRating(@PathVariable long ratingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long clientId = modelMapper.map(auth.getPrincipal(), long.class);

        HttpStatus status = ratingService.deleteRating(ratingId, clientId);
        return ResponseEntity.status(status).build();
    }
}
