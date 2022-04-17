package me.doyoung.jpaconcurrency.reservation.infra;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EntityManager em;

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
        final Reservation fake2 = Reservation.getFakeInstance("fake2");
        final Reservation fake1 = Reservation.getFakeInstance("fake1");

        reservationRepository.saveAndFlush(fake1);

        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);

        // when
        int count = reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);

        // then
        assertEquals(1, count);


        reservationRepository.saveAndFlush(fake2);

        count = reservationRepository.countByCreatedAtBetweenStartAndEndDateTime(startDateTime, endDateTime);

        assertEquals(2, count);
    }

    @Test
    void findByCreatedAtBetweenStartAndEndDateTimeWithLock() {
        // given
        reservationRepository.saveAndFlush(Reservation.getFakeInstance("fake1"));

        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);

        // when
        final List<Reservation> reservations = reservationRepository.findByCreatedAtBetweenStartAndEndDateTimeWithLock(startDateTime, endDateTime);

        // then
        assertEquals(1, reservations.size());
    }


    @Test
    void lock() {


    }
}
