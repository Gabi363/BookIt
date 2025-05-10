package bookit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("discount")
public class DiscountController {

    @GetMapping("/{userId}")
    @Operation(summary = "Get discounts of given user")
    public ResponseEntity<?> getDiscounts(@PathVariable String userId) {
//        @TODO
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{discountId}")
    @Operation(summary = "Get details of discount")
    public ResponseEntity<?> getDiscountDetails(@PathVariable String discountId) {
//        @TODO
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{discountCode}")
    @Operation(summary = "Use discount code for visit")
    public ResponseEntity<?> checkDiscountCode(@PathVariable String discountCode) {
//        @TODO
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{userId}")
    @Operation(summary = "Get numbers of points to get for the next prize")
    public ResponseEntity<?> getStatus(@PathVariable String userId) {
//        @TODO
        return ResponseEntity.ok().build();
    }

}
