package me.doyoung.jpaconcurrency.treatment.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.treatment.application.TreatmentService;
import me.doyoung.jpaconcurrency.treatment.dto.TreatmentDtos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/treatment")
public class TreatmentController {
    private final TreatmentService treatmentService;

    @PostMapping
    public ResponseEntity<TreatmentDtos.Response> saveTreatment(
            @RequestBody TreatmentDtos.Request request) {
        return ResponseEntity.ok().body(treatmentService.save(request));
    }
}
