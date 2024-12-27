package bookit.backend.controller;

import bookit.backend.model.request.AddWorkingHoursRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.WorkingHoursService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("working-hours")
public class WorkingHoursController {

    private final WorkingHoursService workingHoursService;
    private final AccountService accountService;

    @PostMapping("/{businessId}")
    @Operation(summary = "Add working hours")
    public ResponseEntity<?> addWorkingHours(@Valid @RequestBody AddWorkingHoursRequest request,
                                             @PathVariable Long businessId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();

        if(userInfo.isNotBusinessOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = workingHoursService.addWorkingHours(businessId, userInfo.getId(), request);
        return ResponseEntity.status(status).build();
    }

    @PutMapping("/{businessId}")
    @Operation(summary = "Update working hours")
    public ResponseEntity<?> updateWorkingHours(@Valid @RequestBody AddWorkingHoursRequest request,
                                                @PathVariable Long businessId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = workingHoursService.updateWorkingHours(businessId, userInfo.getId(), request);
        return ResponseEntity.status(status).build();
    }
}