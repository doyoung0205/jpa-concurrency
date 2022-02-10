package me.doyoung.jpaconcurrency.reservation.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import me.doyoung.jpaconcurrency.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ReservationControllerTest extends AcceptanceTest {

        public static final String RESERVE_URL = "/reserve";

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
