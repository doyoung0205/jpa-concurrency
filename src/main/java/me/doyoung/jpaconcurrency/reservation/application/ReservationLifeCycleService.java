package me.doyoung.jpaconcurrency.reservation.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationLifeCycleService {
    private final ReservationRepository repository;
    private final ReservationValidator validator;

    /**
     * 예약 등록 기능
     *
     * @throws ConcurrencyFailureException 동시에 예약할 경우 가장 최초인 경우가 아니라면 나타나는 예외
     * @apiNote 동시에 한번에 예약할 경우 가장 최초의 한명만 예약한다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReservationDtos.Response saveReservation(ReservationDtos.Request request) {
        final Reservation reservation = Reservation.from(request.getTreatmentId(), request.getName(), validator);
        final Reservation savedReservation = repository.save(reservation);
        return new ReservationDtos.Response(savedReservation);
    }
}
