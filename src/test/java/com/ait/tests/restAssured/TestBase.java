package com.ait.tests.restAssured;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    public static final String TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoianVyZ2l0YUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTY5OTI1NzA2NywiaWF0IjoxNjk4NjU3MDY3fQ.hpD3-g6ZMjEWwhwF88VLMcUTdW7nj_ZsB_GSlfTHpPc";
    @BeforeMethod
    public void init(){
        System.err.close();
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }
}
