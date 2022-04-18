package me.doyoung.jpaconcurrency.reservation.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.AcceptanceTest;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static me.doyoung.jpaconcurrency.treatment.ui.TreatmentControllerTest.진료가_등록_되어_있음;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ReservationControllerTest extends AcceptanceTest {

    public static final String RESERVE_URL = "/reserve";
    public static final String RESERVE_COUNT_URL = RESERVE_URL + "/count";

    Long 오전진료_아이디;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        오전진료_아이디 = 진료가_등록_되어_있음("오전진료");
    }

    @Test
    @DisplayName("여러명이 동시에 예약한다.")
    void multiReserve() throws InterruptedException {
        final int N_THREAD_COUNT = 50;
        final ExecutorService executorService = Executors.newFixedThreadPool(N_THREAD_COUNT);

        // when
        CountDownLatch latch = new CountDownLatch(N_THREAD_COUNT);
        for (int index = 0; index < N_THREAD_COUNT; index++) {
            final int finalIndex = index;
            executorService.execute(() -> {
                예약하기(예약요청정보가져오기(오전진료_아이디, finalIndex + "번 신규예약자"));
                latch.countDown();
            });

        }
        latch.await(10, TimeUnit.SECONDS);

        final Integer count = 예약자_수_확인하기(오전진료_아이디).as(Integer.class);
        assertEquals(2, count);
    }

    @Test
    @DisplayName("예약한다.")
    void reserve() {
        // given
        Map<String, String> params = 예약요청정보가져오기(오전진료_아이디, "신규예약자");
        // when
        예약하기(params);
        // then
        final Integer count = 예약자_수_확인하기(오전진료_아이디).as(Integer.class);
        assertEquals(1, count);
    }

    private Map<String, String> 예약요청정보가져오기(Long treatmentId, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("treatmentId", String.valueOf(treatmentId));
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

    private ExtractableResponse<Response> 예약자_수_확인하기(Long treatmentId) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params("treatmentId", treatmentId)
                .when()
                .get(RESERVE_COUNT_URL)
                .then().log().all()
                .extract();
        return response;
    }

}
