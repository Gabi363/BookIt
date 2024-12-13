package bookit.backend.controller;

import bookit.backend.model.request.AddReservationRequest;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

//    @GetMapping("/choose-date/{serviceId}")
//    public ReservationOptionsResponse chooseDate(@RequestParam LocalDate date, @PathVariable long serviceId) {
//        return reservationService.getReservationOptions(serviceId, date);
//    }


    @PostMapping("/book/{serviceId}")
    public ResponseEntity<?> makeReservation(@RequestBody AddReservationRequest request,
                                             @PathVariable long serviceId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(!userInfo.isClient()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(reservationService.addReservation(request, serviceId, userInfo.getId())).build();
    }

}
