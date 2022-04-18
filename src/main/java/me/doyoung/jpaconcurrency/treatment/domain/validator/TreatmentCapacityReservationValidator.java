package me.doyoung.jpaconcurrency.treatment.domain.validator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.domain.ReservationCapacityException;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 예약 등록시 최대 인원을 체크하는 유효성 검사
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TreatmentCapacityReservationValidator implements ReservationValidator {
    public static final String RESERVATION_ERROR_MESSAGE_TEMPLATE = "예약인원이 꽉 찼습니다. 최대 인원은 %d 까지 입니다.";

    private final ReservationRepository reservationRepository;
    private final TreatmentRepository treatmentRepository;


    @Override
    @Transactional
    public void validate(Reservation reservation) {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        final Treatment treatment = treatmentRepository.getByIdWithOptimisticForceIncrement(reservation.getTreatmentId());
        final int count = reservationRepository.countByTreatmentIdAndToday(reservation.getTreatmentId(), startDateTime, endDateTime);
        final int capacity = treatment.getCapacity();

        log.info("[ReservationCapacityValidator] 최대인원수 = {} 예약자정보 = {}, 현재 예약자 수 = {}", capacity, reservation, count);
        if (count >= capacity) {
            throw new ReservationCapacityException(String.format(RESERVATION_ERROR_MESSAGE_TEMPLATE, capacity));
        }

    }


}
