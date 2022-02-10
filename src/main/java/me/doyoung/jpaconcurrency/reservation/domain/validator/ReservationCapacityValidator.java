package me.doyoung.jpaconcurrency.reservation.domain.validator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
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
public class ReservationCapacityValidator implements ReservationValidator {
    private static final int MAX_CAPACITY_COUNT = 2;
    public static final String RESERVATION_ERROR_MESSAGE =
            String.format("예약인원이 꽉 찼습니다. 최대 인원은 %d 까지 입니다.", MAX_CAPACITY_COUNT);

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void validate(Reservation reservation) {
        final int count = getReservationCountToday();
        log.info("[validate] 예약자정보 = {}, 현재 예약자 수 = {}", reservation, count);
        if (count >= MAX_CAPACITY_COUNT) {
            throw new IllegalStateException(RESERVATION_ERROR_MESSAGE);
        }
    }

    private int getReservationCountToday() {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);
    }
}
