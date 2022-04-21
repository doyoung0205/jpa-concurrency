package me.doyoung.jpaconcurrency.reservation.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.application.ReservationInfoService;
import me.doyoung.jpaconcurrency.reservation.application.ReservationSyncService;
import me.doyoung.jpaconcurrency.reservation.domain.ReservationCapacityException;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationInfoService infoService;
    private final ReservationSyncService syncService;

    @PostMapping
    public ResponseEntity<ReservationDtos.Response> saveReserve(
            @RequestBody ReservationDtos.Request request) {
        return ResponseEntity.ok().body(syncService.reserveWithSync(request));
    }


    @GetMapping("/count")
    public ResponseEntity<Integer> getReserveCount(Long treatmentId) {
        return ResponseEntity.ok().body(infoService.getTodayReserveCountByTreatmentId(treatmentId));
    }

    @ExceptionHandler(value = {ReservationCapacityException.class})
    public String reservationCapacityExceptionHandler(ReservationCapacityException exception) {
        return exception.getMessage();
    }
}
