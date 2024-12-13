package bookit.backend.controller;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.*;
import bookit.backend.model.response.BusinessListResponse;
import bookit.backend.model.response.BusinessResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.BusinessService;
import bookit.backend.service.ServicesService;
import bookit.backend.service.WorkingHoursService;
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

import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("business")
public class BusinessController {

    private final BusinessService businessService;
    private final AccountService accountService;
    private final ServicesService servicesService;
    private final WorkingHoursService workingHoursService;
    private final ModelMapper modelMapper;

    @GetMapping
    @ManagedOperation(description = "Get list of all businesses")
    public BusinessListResponse getBusinesses() {
        return new BusinessListResponse(businessService.getBusinesses());
    }

    @GetMapping("/{businessId}")
    @ManagedOperation(description = "Get business details")
    public ResponseEntity<?> getBusiness(@PathVariable long businessId) {
        Optional<BusinessDto> business = businessService.getBusiness(businessId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.ok().body(new BusinessResponse(business.get()));
    }

    @GetMapping("/owner/{ownerId}")
    @ManagedOperation(description = "Get business of a user")
    public ResponseEntity<?> getBusinessOfOwner(@PathVariable long ownerId) {
        if(accountService.isUserNotAuthorized(SecurityContextHolder.getContext().getAuthentication(), ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<BusinessDto> business = businessService.getBusinessByOwnerId(ownerId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.ok().body(new BusinessResponse(business.get()));
    }

    @PostMapping
    @ManagedOperation(description = "Add business")
    public ResponseEntity<?> createBusiness(@Valid @RequestBody CreateBusinessRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.BUSINESS_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(businessService.getBusinessByOwnerId(ownerId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<BusinessDto> business = businessService.createBusiness(request, ownerId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new BusinessResponse(business.get()));
    }

    @PutMapping("/update/{ownerId}")
    @ManagedOperation(description = "Update business")
    public ResponseEntity<?> updateBusinessInfo(@Valid @RequestBody CreateBusinessRequest request,
                                                @PathVariable long ownerId) {
        if(accountService.isUserNotAuthorized(SecurityContextHolder.getContext().getAuthentication(), ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(businessService.getBusinessByOwnerId(ownerId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business does not exist!");
        }

        HttpStatus status = businessService.updateBusinessInfo(request, ownerId);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/{businessId}/worker")
    @ManagedOperation(description = "Add worker to business")
    public ResponseEntity<?> addWorker(@Valid @RequestBody CreateUserRequest request,
                                       @PathVariable long businessId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        var b = businessService.getBusinessByOwnerId(ownerId);

        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var worker = accountService.createWorkerAccount(request, businessId);
        if(worker.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{businessId}/worker")
    @ManagedOperation(description = "Delete worker")
    public ResponseEntity<?> deleteWorker(@PathVariable long businessId,
                                          @Valid @RequestBody DeleteUserRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        var b = businessService.getBusinessByOwnerId(ownerId);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN) {
            if(b.isEmpty() || b.get().getId() != businessId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        HttpStatus status = accountService.deleteWorkerUserByOwner(request.getEmail());
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/{businessId}/service")
    @ManagedOperation(description = "Add service to business")
    public ResponseEntity<?> addService(@Valid @RequestBody CreateServiceRequest request,
                                        @PathVariable long businessId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        var b = businessService.getBusinessByOwnerId(ownerId);

        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.addService(businessId, request);
        return ResponseEntity.status(status).build();
    }

    @PutMapping("/{businessId}/service/{serviceId}")
    @ManagedOperation(description = "Update service")
    public ResponseEntity<?> updateService(@Valid @RequestBody CreateServiceRequest request,
                                           @PathVariable long businessId,
                                           @PathVariable long serviceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        var b = businessService.getBusinessByOwnerId(ownerId);

        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.updateService(businessId, request, serviceId);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{businessId}/service/{serviceId}")
    @ManagedOperation(description = "Delete service")
    public ResponseEntity<?> deleteService(@PathVariable long businessId,
                                           @PathVariable long serviceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        var b = businessService.getBusinessByOwnerId(ownerId);

        if(b.isEmpty() || b.get().getId() != businessId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.ADMIN && businessId != b.get().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = servicesService.deleteService(serviceId, businessId);
        return ResponseEntity.status(status).build();
    }

    @PostMapping("/working-hours/{businessId}")
    @ManagedOperation(description = "Add working hours")
    public ResponseEntity<?> addWorkingHours(@Valid @RequestBody AddWorkingHoursRequest request,
                                             @PathVariable Long businessId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.BUSINESS_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = workingHoursService.addWorkingHours(businessId, ownerId, request);
        return ResponseEntity.status(status).build();
    }

    @PutMapping("/working-hours/{businessId}")
    @ManagedOperation(description = "Update working hours")
    public ResponseEntity<?> updateWorkingHours(@Valid @RequestBody AddWorkingHoursRequest request,
                                                @PathVariable Long businessId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long ownerId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(role != UserRole.BUSINESS_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = workingHoursService.updateWorkingHours(businessId, ownerId, request);
        return ResponseEntity.status(status).build();
    }
}
