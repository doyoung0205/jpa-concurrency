package me.doyoung.jpaconcurrency.reservation.ui;

import lombok.RequiredArgsConstructor;
import me.doyoung.jpaconcurrency.reservation.application.ReservationService;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public ResponseEntity<ReservationDtos.Response> reserve(
            @RequestBody ReservationDtos.Request request) {
        return ResponseEntity.ok().body(service.reserve(request));
    }
}
