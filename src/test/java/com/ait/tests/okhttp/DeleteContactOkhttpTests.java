package com.ait.tests.okhttp;

import com.ait.dto.AuthRequestDto;
import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import com.ait.dto.MessageDto;
import com.google.gson.Gson;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeleteContactOkhttpTests {
    String token= "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoianVyZ2l0YUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY5OTAwMjMzMSwiaWF0IjoxNjk4NDAyMzMxfQ.fjDc1XhA9JuhadyKLwet3X-rc_BsHgCAuz65nex8e7A";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    String id;

    @BeforeMethod
    public void precondition() throws IOException {
        //create contact
        ContactDto contactDto = ContactDto.builder()
                .name("Nika")
                .lastName("Bumbum")
                .address("Moon 5")
                .email("moon@mon.com")
                .phone("9876543218")
                .description("the best")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(requestBody)
                .addHeader("Authorization", token)
                .build();
        Response response=client.newCall(request).execute();
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        String message = messageDto.getMessage();
        System.out.println(message);

        //get id from message{Contact was added! ID: 3200ca99-b50b-47af-98a6-49e9919478c7}
        String[] all = message.split(": ");
        id = all[1];


    }
    @Test
    public void deleteContactByIdPositiveTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(messageDto.getMessage());
        Assert.assertEquals(messageDto.getMessage(),"Contact was deleted!");

    }
    @Test
    public void deleteContactNegativeTest() throws IOException {
        // Pateikti neteisingą kontaktą, kurio nėra sistemoje.
        String nonExistentContactId = "000"; // Pakeiskite į neteisingą ID.

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + nonExistentContactId)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        // Tikriname, ar gavome 400 HTTP kodą
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);

        Assert.assertEquals(errorDto.getStatus(),400);
        System.out.println(errorDto.getStatus());
        Assert.assertEquals(errorDto.getMessage(),"Contact with id: 000 not found in your contacts!");
        System.out.println(errorDto.getMessage());

    }

    }

