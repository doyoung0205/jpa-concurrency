package me.doyoung.jpaconcurrency.reservation.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationInfoService {

    private final ReservationRepository repository;

    @Transactional(readOnly = true)
    public int getTodayReserveCountByTreatmentId(Long treatmentId) {
        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);
        return repository.countByTreatmentIdAndToday(treatmentId, startDateTime, endDateTime);
    }
}
