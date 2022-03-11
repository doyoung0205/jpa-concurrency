package me.doyoung.jpaconcurrency.reservation.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.application.ReservationService;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ReservationDtos.Response> reserve(
            @RequestBody ReservationDtos.Request request) {
        log.info("{} 예약 신청 controller 시작", Thread.currentThread().getName());
        final ResponseEntity<ReservationDtos.Response> result = ResponseEntity.ok().body(service.reserve(request));
        log.info("{} 예약 신청 controller 종료", Thread.currentThread().getName());
        return result;
    }
}
