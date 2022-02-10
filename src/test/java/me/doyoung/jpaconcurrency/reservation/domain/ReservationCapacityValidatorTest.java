package me.doyoung.jpaconcurrency.reservation.domain;

import me.doyoung.jpaconcurrency.reservation.domain.validator.ReservationCapacityValidator;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
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

    @Test
    @DisplayName("예약이 2명 이상 되어있는 경우 유효하지 못하다.")
    @Transactional
    void validateFail() {
        // given
        final List<Reservation> reservations = Arrays.asList(Reservation.getFakeInstance("fake1"), Reservation.getFakeInstance("fake2"));
        reservationRepository.saveAll(reservations);

        // when - then
        assertThrows(IllegalStateException.class, () -> {
            validator.validate(Reservation.getFakeInstance("fake3"));
        });
    }

    @Test
    @DisplayName("2명 미만으로 예약되어있는 경우 유효하다")
    @Transactional
    void validateSuccess() {
        // given
        reservationRepository.save(Reservation.getFakeInstance("fake1"));

        // when - then
        assertDoesNotThrow(() -> validator.validate(Reservation.getFakeInstance("fake2")));
    }
}
