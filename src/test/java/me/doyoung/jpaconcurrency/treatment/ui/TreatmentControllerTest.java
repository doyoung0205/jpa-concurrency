package me.doyoung.jpaconcurrency.treatment.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import me.doyoung.jpaconcurrency.AcceptanceTest;
import me.doyoung.jpaconcurrency.treatment.dto.TreatmentDtos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TreatmentControllerTest extends AcceptanceTest {

    private static final String TREATMENT_URL = "/treatment";

    @Test
    @DisplayName("진료를 등록한다.")
    void treatment() {
        // given - when
        final Long response = 진료가_등록_되어_있음("오전진료");

        // then
        assertNotNull(response);
    }


    public static Long 진료가_등록_되어_있음(String name) {
        final Map<String, String> params = 진료요청정보가져오기(name);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post(TREATMENT_URL)
                .then().log().all()
                .extract();
        return response.as(TreatmentDtos.Response.class).getId();
    }

    private static Map<String, String> 진료요청정보가져오기(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }


}
