package me.doyoung.jpaconcurrency.reservation.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationSyncService {
    private final ReservationLifeCycleService reservationLifeCycleService;

    public ReservationDtos.Response reserveWithSync(ReservationDtos.Request request) {
        int retryCount = Treatment.DEFAULT_CAPACITY - 1;
        for (int index = 0; index < retryCount; index++) {
            try {
                return reservationLifeCycleService.saveReservation(request);
            } catch (ConcurrencyFailureException exception) {
                log.info("[reserveWithSync] {} : ConcurrencyFailureException 오류 {} 번 발생", Thread.currentThread().getName(), (index + 1));
            }
        }
        throw new IllegalThreadStateException("동시성을 처리할 수 없는 상태입니다.");
    }

}
