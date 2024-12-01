package bookit.backend.controller;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.CreateBusinessRequest;
import bookit.backend.model.response.BusinessListResponse;
import bookit.backend.model.response.BusinessResponse;
import bookit.backend.service.BusinessService;
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
    private final ModelMapper modelMapper;

    @GetMapping
    @ManagedOperation(description = "Get list of all businesses")
    public BusinessListResponse getBusinesses() {
        return new BusinessListResponse(businessService.getBusinesses());
    }

    @GetMapping("/{ownerId}")
    @ManagedOperation(description = "Get businesses of a user")
    public ResponseEntity<?> getBusiness(@PathVariable long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long currentId = modelMapper.map(auth.getPrincipal(), long.class);
        UserRole role = modelMapper.map(auth.getAuthorities().iterator().next().toString(), UserRole.class);

        if(ownerId != currentId && role != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<BusinessDto> business = businessService.getBusinessByOwnerId(ownerId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist!");
        }

        return ResponseEntity.ok().body(new BusinessResponse(business.get()));
    }

    @PostMapping("/{ownerId}")
    @ManagedOperation(description = "Add business")
    public ResponseEntity<?> createBusiness(@Valid @RequestBody CreateBusinessRequest request,
                                            @PathVariable long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long currentId = modelMapper.map(auth.getPrincipal(), long.class);
        if(ownerId != currentId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(businessService.getBusinessByOwnerId(ownerId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<BusinessDto> business = businessService.createBusiness(request, ownerId);
        if(business.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new BusinessResponse(business.get()));
    }
}
