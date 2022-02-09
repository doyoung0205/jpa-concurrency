package me.doyoung.jpaconcurrency.reservation.application;

import lombok.RequiredArgsConstructor;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.domain.ReservationValidator;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationValidator validator;

    /**
     * 예약 등록 서비스
     *
     * @apiNote 하루에 최대 2명까지만 예약이 가능!
     */
    @Transactional
    public ReservationDtos.Response reserve(ReservationDtos.Request request) {
        final Reservation reservation = Reservation.from(request, validator);
        final Reservation savedReservation = repository.save(reservation);
        return new ReservationDtos.Response(savedReservation);
    }
}
