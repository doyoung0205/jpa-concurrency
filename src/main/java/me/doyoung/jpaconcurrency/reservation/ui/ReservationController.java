package me.doyoung.jpaconcurrency.reservation.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.application.ReservationService;
import me.doyoung.jpaconcurrency.reservation.domain.ReservationCapacityException;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationCapacityValidator.RESERVATION_ERROR_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationDtos.Response> reserve(@RequestBody ReservationDtos.Request request) {
        final String threadName = Thread.currentThread().getName();
        log.info("{} 예약 신청 controller 시작", threadName);
        try {
            final ReservationDtos.Response reserve = service.reserve(request);
            final ResponseEntity<ReservationDtos.Response> result = ResponseEntity.ok().body(reserve);
            log.info("{} 예약 신청 controller 정상 종료", threadName);
            return result;
        } catch (ConcurrencyFailureException exception) {
            log.info("{} 예약 신청 OptimisticLockingFailure 오류 발생", threadName);
            throw new ReservationCapacityException(RESERVATION_ERROR_MESSAGE);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> reserveCount() {
        return ResponseEntity.ok().body(service.reserveCountToday());
    }

    @ExceptionHandler(value = {ReservationCapacityException.class})
    public String reservationCapacityExceptionHandler(ReservationCapacityException exception) {
        return exception.getMessage();
    }
}
