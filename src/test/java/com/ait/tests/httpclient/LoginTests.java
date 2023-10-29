package com.ait.tests.httpclient;

import com.ait.dto.AuthRequestDto;
import com.ait.dto.AuthResponseDto;
import com.ait.dto.ErrorDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginTests {
    @Test
    public void loginPositiveTest() throws IOException {
        String email="jurgita@gmail.com";
        String password="Qwerty123456$";
        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString("{\n" +
                        "  \"username\": \"" + email + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}", ContentType.APPLICATION_JSON)
                .execute();
        System.out.println(response); //sito nezino Java, delto perdarom i json ji. Neaiskus kodas

        String responseJson = response.returnContent().asString();
        System.out.println(responseJson); //sitas irgi mums netinka, nes ismeta su kabutem,skliaustais irp na. delto koreguojame, JSON tipo spausdina

        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement token = element.getAsJsonObject().get("token");
        System.out.println(token.getAsString()); //o sitas jau tinka, grynai tokina ismeta

    }

    @Test
    public void loginPositiveTestWithData() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("jurgita@gmail.com")
                .password("Qwerty123456$")
                .build();
        Gson gson = new Gson();
        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();
        String responseJson = response.returnContent().asString();
        AuthResponseDto responseDto = gson.fromJson(responseJson, AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }
    @Test
    public void loginNegativeWrongPasswordTestWithData() throws IOException {

//cia reiskia naudosime klases is main  (Data)AuthRequest/Authresponse/ErrorDto

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("jurgita@gmail.com")
                .password("Qwerty223456$")
                .build();
        Gson gson = new Gson();
        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();
        HttpResponse httpResponse = response.returnResponse();
        System.out.println(httpResponse);

        System.out.println(httpResponse.getStatusLine().getStatusCode());

        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb=new StringBuilder();
        while ((line=reader.readLine())!=null){
            sb.append(line);
        }
        ErrorDto errorDto = gson.fromJson(sb.toString(), ErrorDto.class);
        System.out.println(errorDto); //sitas spausdinsa visa info is klases
        System.out.println(errorDto.getError()); //cia spausdina pavadinima
        System.out.println(errorDto.getMessage()); //cia tik zinute kas yra ten irasyta
        System.out.println(errorDto.getPath()); //o cia kelia visa
    }
}
