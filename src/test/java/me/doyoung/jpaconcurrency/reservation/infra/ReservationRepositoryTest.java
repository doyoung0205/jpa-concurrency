package me.doyoung.jpaconcurrency.reservation.infra;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
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
    TreatmentRepository treatmentRepository;

    @Autowired
    EntityManager em;
    Long treatmentId;

    @BeforeEach
    void setUp() {
        this.treatmentId = treatmentRepository.save(new Treatment("감기진료")).getId();
    }

    @Test
    void save() {
        // given
        final Reservation reservation = Reservation.getFakeInstance(treatmentId, "fake1");
        // when
        final Reservation savedReservation = reservationRepository.save(reservation);
        // then
        assertNotNull(savedReservation.getId());
    }


    @Test
    void countByTreatmentIdAndTodayWithLock() {

        // given
        reservationRepository.saveAndFlush(Reservation.getFakeInstance(treatmentId, "fake1"));

        final LocalDateTime startDateTime = LocalDate.now().atTime(0, 0);
        final LocalDateTime endDateTime = startDateTime.plusDays(1L);

        // when
        final int count = reservationRepository.countByTreatmentIdAndTodayWithLock(treatmentId, startDateTime, endDateTime);

        // then
        assertEquals(1, count);
    }


}
