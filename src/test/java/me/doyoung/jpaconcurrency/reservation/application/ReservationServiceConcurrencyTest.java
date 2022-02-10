package me.doyoung.jpaconcurrency.reservation.application;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceConcurrencyTest {

    private static final int N_THREAD_COUNT = 5;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(N_THREAD_COUNT);

    @Autowired
    ReservationService service;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    public void init() {
        reservationRepository.saveAndFlush(Reservation.getFakeInstance("기존예약자"));
    }

    @AfterEach
    public void afterEach() {
        reservationRepository.deleteAllInBatch();
    }

    @DisplayName("남은 자리가 한자리 일때, 2명 이상이 거의 동시에 예약을 할 때 한명만 예약된다.")
    @Test
    void reserveConcurrency() throws InterruptedException {
        // given
        // when
        CountDownLatch latch = new CountDownLatch(N_THREAD_COUNT);
        for (int index = 0; index < N_THREAD_COUNT; index++) {
            final int finalIndex = index;
            executorService.execute(() -> {
                log.info("[BEFORE] reserve");
                try {
                    service.reserve(new ReservationDtos.Request("신규예약자" + finalIndex));
                } catch (Exception e) {
                    log.info("[ERROR] {}", e.getMessage());
                }
                log.info("[AFTER] reserve"); // 1번만 찍힘.
                latch.countDown();
            });

        }
        latch.await(10, TimeUnit.SECONDS);

        // then
        assertEquals(getReservationCountByToday(), 2);
    }


    private int getReservationCountByToday() {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);
    }
}
