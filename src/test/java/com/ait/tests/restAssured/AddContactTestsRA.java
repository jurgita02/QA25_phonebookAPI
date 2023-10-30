package com.ait.tests.restAssured;

import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddContactTestsRA extends TestBase{
    @Test
    public void addNewContactSuccessTest(){
        //create contact
        int i=new Random().nextInt(1000)+1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Nika")
                .lastName("Bumbum")
                .address("Moon 5")
                .email("moon"+i+"@mon.com")
                .phone("9876543218"+i)
                .description("the best")
                .build();
        String message = given()
                .header("Authorization", TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message");
        System.out.println(message);
    } @Test
    public void addNewContactWithoutNameTest() {
        //create contact

        ContactDto contactDto = ContactDto.builder()
                .lastName("Bumbum")
                .address("Moon 5")
                .email("moon@mon.com")
                .phone("9876543218")
                .description("the best")
                .build();
        ErrorDto errorDto = given()
                .header("Authorization", TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));
    }
    @Test
    public void addNewContactInvalidPhoneTest() {
        //create contact

        ContactDto contactDto = ContactDto.builder()
                .name("Nika")
                .lastName("Bumbum")
                .address("Moon 5")
                .email("moon@mon.com")
                .phone("987")
                .description("the best")
                .build();
         given()
                .header("Authorization", TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                 .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));

    }
}
