package bookit.backend.controller;

import bookit.backend.model.request.CreateServiceRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.BusinessService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.ServicesService;
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
@RequestMapping("service")
public class ServiceController {

    private final BusinessService businessService;
    private final AccountService accountService;
    private final ServicesService servicesService;

    @PostMapping("/{businessId}")
    @Operation(summary = "Add service to business")
    public ResponseEntity<?> addService(@Valid @RequestBody CreateServiceRequest request,
                                        @PathVariable long businessId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        var b = businessService.getBusinessByOwnerId(userInfo.getId());

        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(userInfo.isNotAdmin() && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.addService(businessId, request);
        return ResponseEntity.status(status).build();
    }

    @PutMapping("/{businessId}/{serviceId}")
    @Operation(summary = "Update service")
    public ResponseEntity<?> updateService(@Valid @RequestBody CreateServiceRequest request,
                                           @PathVariable long businessId,
                                           @PathVariable long serviceId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        var b = businessService.getBusinessByOwnerId(userInfo.getId());
        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(userInfo.isNotAdmin() && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.updateService(businessId, request, serviceId);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{businessId}/{serviceId}")
    @Operation(summary = "Delete service")
    public ResponseEntity<?> deleteService(@PathVariable long businessId,
                                           @PathVariable long serviceId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        var b = businessService.getBusinessByOwnerId(userInfo.getId());
        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(userInfo.isNotAdmin() && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.deleteService(businessId, serviceId);
        return ResponseEntity.status(status).build();
    }
}