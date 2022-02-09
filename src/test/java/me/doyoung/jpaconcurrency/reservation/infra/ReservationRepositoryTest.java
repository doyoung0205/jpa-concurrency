package me.doyoung.jpaconcurrency.reservation.infra;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void save() {
        // given
        final Reservation reservation = Reservation.getFakeInstance("fake1");
        // when
        final Reservation savedReservation = reservationRepository.save(reservation);
        // then
        assertNotNull(savedReservation.getId());
    }

    @Test
    void countByCreateDate() {
        // given
        reservationRepository.saveAndFlush(Reservation.getFakeInstance("fake1"));
        entityManager.clear();

        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);

        // when
        final int count = reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);

        // then
        assertEquals(1, count);

    }
}
