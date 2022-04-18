package me.doyoung.jpaconcurrency.reservation.domain;

import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationCapacityValidator;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReservationCapacityValidatorTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationCapacityValidator validator;
    @Autowired
    TreatmentRepository treatmentRepository;

    Long treatmentId;

    @BeforeEach
    public void init() {
        this.treatmentId = treatmentRepository.save(new Treatment("감기진료")).getId();
    }


    @Test
    @DisplayName("예약이 2명 이상 되어있는 경우 유효하지 못하다.")
    @Transactional
    void validateFail() {
        // given
        final List<Reservation> reservations = Arrays.asList(
                Reservation.getFakeInstance(treatmentId, "fake1"),
                Reservation.getFakeInstance(treatmentId, "fake2")
        );
        reservationRepository.saveAll(reservations);
        final Reservation reservation = Reservation.getFakeInstance(treatmentId, "fake3");

        // when - then
        assertThrows(IllegalStateException.class, () -> validator.validate(reservation));
    }

    @Test
    @DisplayName("2명 미만으로 예약되어있는 경우 유효하다")
    @Transactional
    void validateSuccess() {
        // given
        reservationRepository.save(Reservation.getFakeInstance(treatmentId, "fake1"));

        // when - then
        assertDoesNotThrow(() -> validator.validate(Reservation.getFakeInstance(treatmentId, "fake2")));
    }
}
