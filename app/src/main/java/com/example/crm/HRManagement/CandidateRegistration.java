package com.example.crm.HRManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.crm.Model.Candidate;
import com.example.crm.R;
import com.example.crm.Retro.RetroInterface;
import com.example.crm.Retro.Retrofi;
import com.example.crm.citystate.Cities;
import com.example.crm.citystate.Rinterface;
import com.example.crm.citystate.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CandidateRegistration extends AppCompatActivity  {

    EditText name, phone, alt_phone, personal_email, official_email, source, address;
    Spinner city, state;
    Button btn_update;
    List<String> stateList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_registration);

        name = findViewById(R.id.candidate_name);
        phone = findViewById(R.id.candidate_phone);
        alt_phone = findViewById(R.id.candidate_alt_phone);
        personal_email = findViewById(R.id.candidate_personal_email);
        official_email = findViewById(R.id.candidate_official_email);
        source = findViewById(R.id.candidate_source);
        address = findViewById(R.id.candidate_address);
        btn_update = findViewById(R.id.candidate_resume);

        city = findViewById(R.id.candidate_city);
        state = findViewById(R.id.candidate_state);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Spinner_items_city, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter);

        ArrayAdapter<CharSequence> stateadapter = ArrayAdapter.createFromResource(this, R.array.Spinner_items_state, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stateadapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/fayazara/Indian-Cities-API/master/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Rinterface rinterface = retrofit.create(Rinterface.class);
        Call<object> call = rinterface.getObject();
        call.enqueue(new Callback<object>() {
            @Override
            public void onResponse(Call<object> call, Response<object> response) {
                if (!response.isSuccessful()) {
                    System.out.println("response.code() = " + response.code());
                }

                List<Cities> cities = response.body().getCities();
                for (Cities cities1 : cities) {
                    stateList.add(cities1.getState());
                }
                List<String> filteredStateList = removeDuplicates(stateList);
                Collections.sort(filteredStateList);
                filteredStateList.add(0, "Select State");
                state.setAdapter(new ArrayAdapter<>(CandidateRegistration.this, android.R.layout.simple_spinner_dropdown_item, filteredStateList));
                state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String state1 = state.getSelectedItem().toString();
                        cityList.clear();
                        for (Cities cities1 : cities) {
                            if (cities1.getState().equals(state1)) {
                                cityList.add(cities1.getCity());
                            }
                        }
                        List<String> filteredCityList = removeDuplicates(cityList);
                        Collections.sort(filteredCityList);
                        filteredCityList.add(0, "Select City");
                        city.setAdapter(new ArrayAdapter<>(CandidateRegistration.this, android.R.layout.simple_spinner_dropdown_item, filteredCityList));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<object> call, Throwable t) {
                System.out.println("t.getMessage() = " + t.getMessage());
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CandidateRegistration.this, CandidateRemark.class);
                startActivity(intent);
            }
        });

    }
private void CandidateRegister(Candidate candidate){
    RetroInterface retroInterface= Retrofi.initretro().create(RetroInterface.class);
Call<Candidate> call=retroInterface.addCandidate(candidate);
call.enqueue(new Callback<Candidate>() {
    @Override
    public void onResponse(Call<Candidate> call, Response<Candidate> response) {
        if (!response.isSuccessful()) {
            System.out.println(response.code());
        }
    }

    @Override
    public void onFailure(Call<Candidate> call, Throwable t) {
        System.out.println(t.getMessage());
    }
});
}
    private List<String> removeDuplicates(List<String> stateList) {
        List<String> statesList = new ArrayList<>();
        for (String state : stateList) {
            if (!statesList.contains(state)) {
                statesList.add(state);
            }
        }
        return statesList;
    }

}