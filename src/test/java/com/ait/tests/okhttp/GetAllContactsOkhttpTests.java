package com.ait.tests.okhttp;

import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import com.ait.dto.GetAllContactsDto;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttpTests {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoianVyZ2l0YUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY5OTAwMjMzMSwiaWF0IjoxNjk4NDAyMzMxfQ.fjDc1XhA9JuhadyKLwet3X-rc_BsHgCAuz65nex8e7A";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void getAllContactsPositiveTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        GetAllContactsDto contactsDto = gson.fromJson(response.body().string(), GetAllContactsDto.class);
        List<ContactDto> contacts = contactsDto.getContacts();
        for (ContactDto c : contacts) {
            System.out.println(c.getId());
            System.out.println(c.getEmail());
        }
    }
    @Test
    public void getAllContactsNegativeTest() throws IOException{
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts"+ "/invalid-path")  // Neegzistuojantis kelias
                .get()
                .build();
        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 403);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto);
        System.out.println(response.code());
        Assert.assertEquals(response.code(),403);
      //  System.out.println(errorDto.getStatus());
       // Assert.assertEquals(errorDto.getMessage(),"Contact with id: 000 not found in your contacts!");
       // System.out.println(errorDto.getMessage());
    }
    @Test
    public void getAllContactsWithWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", "hhhhhhhh")
                .build();
        Response response = client.newCall(request).execute();
ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
Assert.assertEquals(errorDto.getError(),"Unauthorized");
    }

    }

