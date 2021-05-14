package com.example.crm.HRManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                Check();
            }
        });

    }

    private void Check() {
        String canname = name.getText().toString();
        String canphone = phone.getText().toString();
        String canpersonalemail = personal_email.getText().toString();
        String canaltphone = alt_phone.getText().toString();
        String canofficialemail = official_email.getText().toString();
        String canaddress = address.getText().toString();
        String cansource = source.getText().toString();
        String canstate = state.getSelectedItem().toString();
        String cancity = city.getSelectedItem().toString();
        if (canphone.length() != 10) {
            phone.setError("Please Enter Valid Phone Number ");
            phone.requestFocus();
        } else if (canname.isEmpty()) {
            name.setError("Please Enter  Name");
            name.requestFocus();
        } else if (!canpersonalemail.contains("@")) {
            personal_email.setError("Please Enter a Valid Email Address");
            personal_email.requestFocus();
        } else if (canaddress.isEmpty()) {
            address.setError("Please Enter a Address");
            address.requestFocus();

        } else if (cansource.isEmpty()) {
            source.setError("Please Enter Your Password");
            source.requestFocus();

        } else if (!canofficialemail.contains("@")) {
            official_email.setError("Please Enter a Valid Email Address");
            official_email.requestFocus();
        } else if (canaltphone.length() != 10) {
            alt_phone.setError("Please Enter Valid Phone Number ");
            alt_phone.requestFocus();
        } else if (canstate.contains("Select State")) {
            Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (cancity.contains("Select City")) {
            Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("personalemail", canpersonalemail);
            bundle.putString("officialemail", canofficialemail);
            bundle.putString("source", cansource);
            bundle.putString("address", canaddress);
            bundle.putString("altphone", canaltphone);
            bundle.putString("phone", canphone);
            bundle.putString("name", canname);
            bundle.putString("state", canstate);
            bundle.putString("city", cancity);
            Intent intent = new Intent(CandidateRegistration.this, CandidateRemark.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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