package me.doyoung.jpaconcurrency.reservation.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.AcceptanceTest;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ReservationControllerTest extends AcceptanceTest {

    public static final String RESERVE_URL = "/reserve";

    @Test
    @DisplayName("여러명이 동시에 예약한다.")
    void multiReserve() throws InterruptedException {
        final int N_THREAD_COUNT = 5;
        final ExecutorService executorService = Executors.newFixedThreadPool(N_THREAD_COUNT);

        // when
        CountDownLatch latch = new CountDownLatch(N_THREAD_COUNT);
        for (int index = 0; index < N_THREAD_COUNT; index++) {
            final int finalIndex = index;
            executorService.execute(() -> {
                log.info("[BEFORE] 예약 전");
                final ExtractableResponse<Response> response = 예약하기(예약요청정보가져오기("신규예약자" + finalIndex));
                log.info("[AFTER] 예약 후 {}", response.body().jsonPath());
                latch.countDown();
            });

        }
        latch.await(10, TimeUnit.SECONDS);

    }

    @Test
    @DisplayName("예약한다.")
    void reserve() {
        // given
        Map<String, String> params = 예약요청정보가져오기("신규예약자");
        // when
        final ExtractableResponse<Response> response = 예약하기(params);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Map<String, String> 예약요청정보가져오기(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private ExtractableResponse<Response> 예약하기(Map<String, String> params) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post(RESERVE_URL)
                .then().log().all()
                .extract();
        return response;
    }

}
