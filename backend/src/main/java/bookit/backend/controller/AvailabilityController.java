package bookit.backend.controller;

import bookit.backend.model.request.AddAvailabilityRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.AvailabilityService;
import bookit.backend.service.LoggedUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("availability")
public class AvailabilityController {

    private final AccountService accountService;
    private final AvailabilityService availabilityService;

    @PostMapping
    @Operation(summary = "Add availability for worker")
    public ResponseEntity<?> addAvailability(@RequestBody AddAvailabilityRequest request) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(availabilityService.addAvailability(request, userInfo)).build();
    }

    @GetMapping
    @Operation(summary = "Get workers availabilities for time range")
    public ResponseEntity<?> getAvailabilities(@RequestParam String startDate, @RequestParam String endDate) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner() && !userInfo.isWorker()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(availabilityService.getAllAvailabilities(startDate, endDate, userInfo));
    }

    @GetMapping("/{workerId}")
    @Operation(summary = "Get availabilities for one worker")
    public ResponseEntity<?> getAvailabilitiesForWorker(@PathVariable Long workerId, @RequestParam String startDate, @RequestParam String endDate) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner()){
            if(!userInfo.isWorker() || !userInfo.getId().equals(workerId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.ok(availabilityService.getAvailabilitiesForWorker(workerId, startDate, endDate, userInfo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete availability")
    public ResponseEntity<?> deleteAvailability(@PathVariable Long id) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(availabilityService.deleteAvailability(id)).build();
    }
}
