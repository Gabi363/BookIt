package bookit.backend.controller;

import bookit.backend.model.dto.ReservationDto;
import bookit.backend.model.request.AddReservationRequest;
import bookit.backend.model.response.ReservationsListResponse;
import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final AccountService accountService;

    @GetMapping("/choose-date/{serviceId}")
    public ResponseEntity<?> chooseDate(@RequestParam String date, @PathVariable long serviceId) {
        return ResponseEntity.ok(reservationService.getReservationOptions(serviceId, date));
    }

    @GetMapping("/day")
    @Operation(summary = "Get reservations for given day")
    public ResponseEntity<?> getReservationsForDay(@RequestParam String date) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        List<ReservationDto> reservations;
        if(!userInfo.isNotBusinessOwner()) {
            reservations = reservationService.getReservationsForBusinessOwner(userInfo.getId());
        }
        else if(userInfo.isWorker()) {
            reservations = reservationService.getReservationsForWorker(userInfo.getId());
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(reservationService.filterReservationsForDay(date, reservations));
    }


    @PostMapping("/book/{serviceId}")
    @Operation(summary = "Make the reservation for an appointment")
    public ResponseEntity<?> makeReservation(@RequestBody AddReservationRequest request,
                                             @PathVariable long serviceId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(!userInfo.isClient()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(reservationService.addReservation(request, serviceId, userInfo.getId())).build();
    }

    @GetMapping()
    @Operation(summary = "Get all reservations booked for current user - client, worker or business owner (all reservations for business)")
    public ResponseEntity<?> getReservationsForUser() {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        List<ReservationDto> reservations = null;
        if(!userInfo.isNotBusinessOwner()) {
            reservations = reservationService.getReservationsForBusinessOwner(userInfo.getId());
        }
        else if(userInfo.isWorker()) {
            reservations = reservationService.getReservationsForWorker(userInfo.getId());
        }
        else if(userInfo.isClient()) {
            reservations = reservationService.getReservationsForClient(userInfo.getId());
        }
        if(reservations == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ReservationsListResponse(reservations));
    }

    @DeleteMapping("/{reservationId}")
    @Operation(summary = "Delete given reservation")
    public ResponseEntity<?> deleteReservation(@PathVariable long reservationId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(userInfo.isWorker()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.status(reservationService.deleteReservation(reservationId, userInfo)).build();
    }

    @GetMapping("/calendar/{reservationId}")
    @Operation(summary = "Get ics file for chosen reservation")
    public ResponseEntity<?> getReservationsCalendar(@PathVariable long reservationId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        if(!userInfo.isClient() && !userInfo.isWorker()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        try {
            Calendar calendar = reservationService.exportCalendar(userInfo.getId(), reservationId);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event.ics");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/calendar");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}