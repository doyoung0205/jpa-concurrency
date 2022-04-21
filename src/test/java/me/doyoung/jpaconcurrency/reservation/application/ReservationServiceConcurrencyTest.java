package me.doyoung.jpaconcurrency.reservation.application;

import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
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

    @Autowired
    ReservationService service;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TreatmentRepository treatmentRepository;

    Long treatmentId;

    @BeforeEach
    void setUp() {
        this.treatmentId = treatmentRepository.save(new Treatment("감기진료")).getId();
    }

    @AfterEach
    public void afterEach() {
        reservationRepository.deleteAllInBatch();
    }

    @DisplayName("여러명이 동시에 예약을 할 때 최초 한명만 예약된다.")
    @Test
    void reserveConcurrency() throws InterruptedException {
        // given
        final int N_THREAD_COUNT = 5;
        final ExecutorService executorService = Executors.newFixedThreadPool(N_THREAD_COUNT);

        // when
        CountDownLatch latch = new CountDownLatch(N_THREAD_COUNT);
        for (int index = 0; index < N_THREAD_COUNT; index++) {
            final int finalIndex = index;
            executorService.execute(() -> {
                log.info("[BEFORE] reserve");
                try {
                    service.reserve(new ReservationDtos.Request(treatmentId, "신규예약자" + finalIndex));
                } catch (Exception e) {
                    log.info("[ERROR] {} {}", e.getClass(), e.getMessage());
                }
                log.info("[AFTER] reserve");
                latch.countDown();
            });

        }
        latch.await(10, TimeUnit.SECONDS);

        // then
        assertEquals(getReservationCountByToday(), 1);
    }

    private int getReservationCountByToday() {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return reservationRepository.countByTreatmentIdAndToday(treatmentId, startDateTime, endDateTime);
    }
}
