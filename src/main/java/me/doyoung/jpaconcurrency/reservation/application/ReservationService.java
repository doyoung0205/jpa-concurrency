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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository repository;
    private final ReservationValidator validator;

    /**
     * 예약 등록 기능
     *
     * @throws ConcurrencyFailureException
     * @apiNote 하루에 최대 2명까지만 예약이 가능!
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ReservationDtos.Response reserve(ReservationDtos.Request request) {
        final Reservation reservation = Reservation.from(request.getTreatmentId(), request.getName(), validator);
        final Reservation savedReservation = repository.save(reservation);
        return new ReservationDtos.Response(savedReservation);
    }

    @Transactional(readOnly = true)
    public int getReserveCountByTreatmentIdAndToday(Long treatmentId) {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return repository.countByTreatmentIdAndToday(treatmentId, startDateTime, endDateTime);
    }
}
