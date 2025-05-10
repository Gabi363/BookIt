package bookit.backend.controller;

import bookit.backend.model.request.AddPrizeRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.PrizeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("prizes")
public class PrizeController {

    private final AccountService accountService;
    private final PrizeService prizeService;

    @GetMapping
    @Operation(summary = "Get all prizes")
    public ResponseEntity<?> getPrizes() {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(prizeService.getPrizes());
    }

    @PostMapping
    @Operation(summary = "Add prize")
    public ResponseEntity<?> addPrize(@RequestBody AddPrizeRequest request) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isNotAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = prizeService.addPrize(request);
        return ResponseEntity.status(status).build();
    }

    @DeleteMapping("/{prizeId}")
    @Operation(summary = "Delete prize")
    public ResponseEntity<?> deletePrize(@PathVariable String prizeId) {
//        @TODO
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{prizeId}")
    @Operation(summary = "Edit prize")
    public ResponseEntity<?> editPrize(@PathVariable String prizeId) {
//        @TODO
        return ResponseEntity.ok().build();
    }
}
