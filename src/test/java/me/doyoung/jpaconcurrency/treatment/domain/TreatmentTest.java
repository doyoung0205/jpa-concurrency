package me.doyoung.jpaconcurrency.treatment.domain;

import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TreatmentTest {
    @Autowired
    TreatmentRepository treatmentRepository;

    @Test
    void save() {
        final Treatment treatment = new Treatment("오전진료");
        final Treatment savedTreatment = treatmentRepository.save(treatment);
        assertThat(savedTreatment.getId()).isNotNull();
    }
}
