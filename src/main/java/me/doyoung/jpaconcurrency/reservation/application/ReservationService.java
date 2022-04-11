package me.doyoung.jpaconcurrency.reservation.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationValidator validator;

    /**
     * 예약 등록 기능
     *
     * @param request
     * @apiNote 하루에 최대 2명까지만 예약이 가능!
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ReservationDtos.Response reserve(ReservationDtos.Request request) {
        final Reservation reservation = Reservation.from(request.getName(), validator);
        final Reservation savedReservation = repository.save(reservation);
        return new ReservationDtos.Response(savedReservation);
    }
}
