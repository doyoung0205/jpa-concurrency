package me.doyoung.jpaconcurrency.treatment.application;

import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TreatmentServiceTest {

    @Autowired
    TreatmentService treatmentService;
    @Autowired
    TreatmentRepository treatmentRepository;

    @BeforeEach
    void setUp() {
        treatmentRepository.deleteAllInBatch();
    }

    @Test
    void getByIdWithOptimisticForceIncrement() {

        final Treatment treatment = treatmentRepository.save(new Treatment("오전진료"));
        final Treatment findTreatment = treatmentService.getByIdWithOptimisticForceIncrement(treatment.getId());

        assertThat(treatment.getVersion().intValue()).isZero();
        assertThat(findTreatment.getVersion().intValue()).isEqualTo(1);
    }
}
