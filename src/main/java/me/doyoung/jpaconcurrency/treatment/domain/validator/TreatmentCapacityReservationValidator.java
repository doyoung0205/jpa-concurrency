package me.doyoung.jpaconcurrency.treatment.domain.validator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.reservation.application.ReservationInfoService;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.domain.ReservationCapacityException;
import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationValidator;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예약 등록시 최대 인원을 체크하는 유효성 검사
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TreatmentCapacityReservationValidator implements ReservationValidator {
    public static final String RESERVATION_ERROR_MESSAGE_TEMPLATE = "예약인원이 꽉 찼습니다. 최대 인원은 %d 까지 입니다.";

    private final ReservationInfoService reservationInfoService;
    private final TreatmentRepository treatmentRepository;


    @Override
    @Transactional
    public void validate(Reservation reservation) {
        final Treatment treatment = treatmentRepository.getByIdWithOptimisticForceIncrement(reservation.getTreatmentId());
        final int reserveCount = reservationInfoService.getTodayReserveCountByTreatmentId(reservation.getTreatmentId());
        final int capacity = treatment.getCapacity();

        log.info("[ReservationCapacityValidator] 최대인원수 = {} 예약자정보 = {}, 현재 예약자 수 = {}", capacity, reservation, reserveCount);
        if (reserveCount >= capacity) {
            throw new ReservationCapacityException(String.format(RESERVATION_ERROR_MESSAGE_TEMPLATE, capacity));
        }
    }


}
