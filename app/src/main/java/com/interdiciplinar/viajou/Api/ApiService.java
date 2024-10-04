package com.interdiciplinar.viajou.Api;

import com.interdiciplinar.viajou.Models.Photo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/photos")
    Call<List<Photo>> getPhotos();
}
