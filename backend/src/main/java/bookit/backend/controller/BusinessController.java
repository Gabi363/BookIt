package bookit.backend.controller;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.BusinessFiltersDto;
import bookit.backend.model.request.CreateBusinessRequest;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.DeleteUserRequest;
import bookit.backend.model.response.BusinessListAndFiltersResponse;
import bookit.backend.model.response.BusinessResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.BusinessService;
import bookit.backend.service.LoggedUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("business")
public class BusinessController {

    private final BusinessService businessService;
    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Get list of all businesses")
    public BusinessListAndFiltersResponse getBusinesses() {
        return new BusinessListAndFiltersResponse(businessService.getBusinesses(),
                                                    businessService.getBusinessTypes(),
                                                    businessService.getCities());
    }

    @GetMapping("/filters")
    @Operation(summary = "Get businesses by filters")
    public BusinessListAndFiltersResponse getBusinessesByFilters(@RequestBody BusinessFiltersDto filters) {
        return new BusinessListAndFiltersResponse(businessService.getBusinessesByFilters(filters),
                                                    businessService.getBusinessTypes(),
                                                    businessService.getCities());
    }

    @GetMapping("/{businessId}")
    @Operation(summary = "Get business details")
    public ResponseEntity<?> getBusiness(@PathVariable long businessId) {
        Optional<BusinessDto> business = businessService.getBusiness(businessId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.ok().body(new BusinessResponse(business.get()));
    }

    @GetMapping("/owner")
    @Operation(summary = "Get business of a user")
    public ResponseEntity<?> getBusinessOfOwner() {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();

        if(userInfo.isNotAdmin() && userInfo.isNotBusinessOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<BusinessDto> business = businessService.getBusinessByOwnerId(userInfo.getId());
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.ok().body(new BusinessResponse(business.get()));
    }

    @PostMapping
    @Operation(summary = "Add business")
    public ResponseEntity<?> createBusiness(@Valid @RequestBody CreateBusinessRequest request) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotBusinessOwner()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if(businessService.getBusinessByOwnerId(userInfo.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<BusinessDto> business = businessService.createBusiness(request, userInfo.getId());
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new BusinessResponse(business.get()));
    }

    @PutMapping("/update")
    @Operation(summary = "Update business")
    public ResponseEntity<?> updateBusinessInfo(@Valid @RequestBody CreateBusinessRequest request) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotAdmin() && userInfo.isNotBusinessOwner()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(businessService.getBusinessByOwnerId(userInfo.getId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        HttpStatus status = businessService.updateBusinessInfo(request, userInfo.getId());
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/{businessId}/worker")
    @Operation(summary = "Add worker to business")
    public ResponseEntity<?> addWorker(@Valid @RequestBody CreateUserRequest request,
                                       @PathVariable long businessId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        var b = businessService.getBusinessByOwnerId(userInfo.getId());
        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(userInfo.isNotAdmin() && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var worker = accountService.createWorkerAccount(request, businessId);
        if(worker.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{businessId}/worker")
    @Operation(summary = "Delete worker")
    public ResponseEntity<?> deleteWorker(@PathVariable long businessId,
                                          @Valid @RequestBody DeleteUserRequest request) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        var b = businessService.getBusinessByOwnerId(userInfo.getId());
        if(userInfo.isNotAdmin()) {
            if(b.isEmpty() || b.get().getId() != businessId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        HttpStatus status = accountService.deleteWorkerUserByOwner(request.getEmail());
        return ResponseEntity.status(status).build();
    }

}