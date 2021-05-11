package com.example.crm.Retro;

import com.example.crm.Model.Candidate;
import com.example.crm.citystate.object;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroInterface {
    @POST("candidate_registerapi.php")
    Call<Candidate> addCandidate(@Body Candidate candidate);
}
