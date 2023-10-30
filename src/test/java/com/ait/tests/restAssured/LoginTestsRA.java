package com.ait.tests.restAssured;

import com.ait.dto.AuthRequestDto;
import com.ait.dto.AuthResponseDto;
import com.ait.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginTestsRA extends TestBase {
    AuthRequestDto requestDto = AuthRequestDto.builder()
            .username("jurgita@gmail.com")
            .password("Qwerty123456$")
            .build();

    @Test
    public void loginSuccessPositiveTest() {
        AuthResponseDto dto = given()
                .contentType("application/json")
                .body(requestDto)
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);
        System.out.println(dto.getToken());
    }

    @Test
    public void loginSuccessTest2() {
        String responseToken = given()
                .contentType("application/json")
                .body(requestDto)
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .body(containsString("token"))
                .extract().path("token");
        System.out.println(responseToken);


    }

    @Test
    public void loginWrongEmailTest2() {
        given()
                .contentType(ContentType.JSON)
                .body(AuthRequestDto.builder()
                        .username("jurgitqa@gmail.com")
                        .password("Qwerty123456$")
                        .build())
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("error", equalTo("Unauthorized"));

    }
}
