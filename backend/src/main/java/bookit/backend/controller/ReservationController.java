package bookit.backend.controller;

import bookit.backend.model.dto.ReservationDto;
//import bookit.backend.model.request.AddReservationRequest;
//import bookit.backend.model.response.ReservationsListResponse;
import bookit.backend.service.AccountService;
//import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final AccountService accountService;

//    @GetMapping("/choose-date/{serviceId}")
//    public ReservationOptionsResponse chooseDate(@RequestParam LocalDate date, @PathVariable long serviceId) {
//        return reservationService.getReservationOptions(serviceId, date);
//    }


//    @PostMapping("/book/{serviceId}")
//    public ResponseEntity<?> makeReservation(@RequestBody AddReservationRequest request,
//                                             @PathVariable long serviceId) {
//        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
//        if(!userInfo.isClient()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        return ResponseEntity.status(reservationService.addReservation(request, serviceId, userInfo.getId())).build();
//    }

//    @GetMapping()
//    public ResponseEntity<?> getReservationsForUser() {
//        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
//        List<ReservationDto> reservations = null;
//        if(!userInfo.isNotBusinessOwner()) {
//            reservations = reservationService.getReservationsForBusinessOwner(userInfo.getId());
//        }
//        else if(userInfo.isWorker()) {
//            reservations = reservationService.getReservationsForWorker(userInfo.getId());
//        }
//        else if(userInfo.isClient()) {
//            reservations = reservationService.getReservationsForClient(userInfo.getId());
//        }
//        if(reservations == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//
//        return ResponseEntity.status(HttpStatus.OK).body(new ReservationsListResponse(reservations));
//    }
//
//    @DeleteMapping("/{reservationId}")
//    public ResponseEntity<?> deleteReservation(@PathVariable long reservationId) {
//        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
//        if(userInfo.isWorker()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//
//        return ResponseEntity.status(reservationService.deleteReservation(reservationId, userInfo)).build();
//    }
}
