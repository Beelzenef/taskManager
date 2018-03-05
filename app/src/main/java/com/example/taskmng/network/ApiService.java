package com.example.taskmng.network;

import com.example.taskmng.model.Email;
import com.example.taskmng.model.Task;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by paco on 6/02/18.
 */

public interface ApiService {
    @GET("api/tasks")
    Call<ArrayList<Task>> getTasks();

    @POST("api/tasks")
    Call<Task> createNewTask(@Body Task task);

    @PUT("api/tasks/{id}")
    Call<Task> updateTask(@Body Task task, @Path("id") int id);

    @DELETE("api/tasks/{id}")
    Call<ResponseBody> deleteTask(@Path("id") int id);

    @POST("api/email")
    Call<ResponseBody> sendEmail(@Body Email email);
}

