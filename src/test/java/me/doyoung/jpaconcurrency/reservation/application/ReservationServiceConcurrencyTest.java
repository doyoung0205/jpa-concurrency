package me.doyoung.jpaconcurrency.reservation.application;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    private static final int N_THREADS = 5;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

    @Autowired
    ReservationService service;

    @Autowired
    ReservationRepository reservationRepository;

    @DisplayName("남은 자리가 한자리 일때, 2명 이상이 거의 동시에 예약을 할 때 한명만 예약된다.")
    @Test
    @Transactional
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void reserveConcurrency() throws InterruptedException {
        // given
        reservationRepository.save(Reservation.getFakeInstance("fake2"));

        // when
        CountDownLatch latch = new CountDownLatch(N_THREADS);
        for (int index = 0; index < N_THREADS; index++) {
            final int finalIndex = index;
            executorService.execute(() -> {
                service.reserve(new ReservationDtos.Request("request" + finalIndex));
                latch.countDown();
            });
        }
        latch.await();

        // then
        assertEquals(getReservationCountByToday(), 2);
    }


    private int getReservationCountByToday() {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);
    }
}
