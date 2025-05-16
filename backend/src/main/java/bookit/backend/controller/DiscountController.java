package bookit.backend.controller;

import bookit.backend.model.response.DiscountResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.DiscountService;
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
@RequestMapping("discount")
public class DiscountController {

    private final AccountService accountService;
    private final DiscountService discountService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get discounts of given user")
    public ResponseEntity<?> getDiscounts(@PathVariable long userId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.getId() != userId) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok().body(new DiscountResponse(discountService.getUserPrizes(userId)));
    }

    @PostMapping("/{discountCode}")
    @Operation(summary = "Use discount code for visit")
    public ResponseEntity<?> checkDiscountCode(@PathVariable String discountCode) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isClient()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok().body(discountService.checkDiscountCode(discountCode)
                ? "Pomyślnie użyto kodu zniżkowego"
                : "Kod został już wcześniej użyty lub jest niepoprawny");
    }

}
