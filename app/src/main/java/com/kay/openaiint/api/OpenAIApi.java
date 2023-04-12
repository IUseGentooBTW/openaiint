package com.kay.openaiint.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIApi {

    @Headers("Content-Type: application/json")
    @POST("your_api_endpoint")
    Call<String> generateResponse(@Body String userInput);
}
