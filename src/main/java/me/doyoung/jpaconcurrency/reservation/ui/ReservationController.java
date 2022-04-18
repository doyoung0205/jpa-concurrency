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


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationDtos.Response> saveReserve(
            @RequestBody ReservationDtos.Request request) {

        int retryCount = 3;
        log.info("{} 예약 신청 controller 시작", Thread.currentThread().getName());

        for (int i = 0; i < retryCount; i++) {
            try {
                return getResponseResponseEntity(request);
            } catch (ConcurrencyFailureException exception) {
                log.info("{} 예약 신청 OptimisticLockingFailure 오류 {} 번 발생", Thread.currentThread().getName(), (i + 1));
            }
        }
        throw new ReservationCapacityException("예약인원이 꽉 찼습니다.");

    }

    private ResponseEntity<ReservationDtos.Response> getResponseResponseEntity(ReservationDtos.Request request) {
        final ReservationDtos.Response reserve = service.reserve(request);
        final ResponseEntity<ReservationDtos.Response> result = ResponseEntity.ok().body(reserve);
        log.info("{} 예약 신청 controller 정상 종료", Thread.currentThread().getName());
        return result;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getReserveCount(Long treatmentId) {
        return ResponseEntity.ok().body(service.getReserveCountByTreatmentIdAndToday(treatmentId));
    }

    @ExceptionHandler(value = {ReservationCapacityException.class})
    public String reservationCapacityExceptionHandler(ReservationCapacityException exception) {
        return exception.getMessage();
    }
}
