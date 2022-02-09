package me.doyoung.jpaconcurrency.reservation.domain;


import lombok.RequiredArgsConstructor;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 예약 등록시 최대 인원을 체크하는 유효성 검사
 */
@Component
@RequiredArgsConstructor
public class ReservationCapacityValidator implements ReservationValidator {
    private static final int MAX_CAPACITY_COUNT = 2;
    public static final String RESERVATION_ERROR_MESSAGE =
            String.format("예약인원이 꽉 찼습니다. 최대 인원은 %d 까지 입니다.", MAX_CAPACITY_COUNT);

    private final ReservationRepository reservationRepository;

    @Override
    public void validate(Reservation reservation) {
        final int count = getReservationCountByToday();
        if (count >= MAX_CAPACITY_COUNT) {
            throw new IllegalStateException(RESERVATION_ERROR_MESSAGE);
        }
    }

    private int getReservationCountByToday() {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);
    }
}
