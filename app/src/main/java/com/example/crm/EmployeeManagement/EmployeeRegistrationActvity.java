package com.example.crm.EmployeeManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.crm.CRMManagement.CRM_RegisterActivity;
import com.example.crm.Model.Candidate;
import com.example.crm.Model.Employee;
import com.example.crm.R;
import com.example.crm.Retro.RetroInterface;
import com.example.crm.Retro.Retrofi;
import com.example.crm.citystate.Cities;
import com.example.crm.citystate.Rinterface;
import com.example.crm.citystate.object;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeRegistrationActvity extends AppCompatActivity {

    Spinner stateSpin, citySpin, genderSpin;
    List<String> stateList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();
    Button register;
    RadioButton job, intern;
    ExpandableLayout expandablemycontent, expandableinterncontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_registration_actvity);

        stateSpin = findViewById(R.id.state);
        citySpin = findViewById(R.id.city);
        register = findViewById(R.id.btn_register);
        job = findViewById(R.id.job);
        intern = findViewById(R.id.intern);
        genderSpin = findViewById(R.id.gender);

        register.setOnClickListener(v -> {
            Intent intent =  new Intent(EmployeeRegistrationActvity.this, EmployeeRegisterSecondActivity.class);
            startActivity(intent);
        });

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
                stateSpin.setAdapter(new ArrayAdapter<>(EmployeeRegistrationActvity.this, android.R.layout.simple_spinner_dropdown_item, filteredStateList));
                stateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String state = stateSpin.getSelectedItem().toString();
                        cityList.clear();
                        for (Cities cities1 : cities) {
                            if (cities1.getState().equals(state)) {
                                cityList.add(cities1.getCity());
                            }
                        }
                        List<String> filteredCityList = removeDuplicates(cityList);
                        Collections.sort(filteredCityList);
                        filteredCityList.add(0, "Select City");
                        citySpin.setAdapter(new ArrayAdapter<>(EmployeeRegistrationActvity.this, android.R.layout.simple_spinner_dropdown_item, filteredCityList));

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
//        List<String> tasks = Arrays.asList(getResources().getStringArray(R.array.tasks));
//        taskSpin.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tasks));
        genderSpin.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Male", "Female", "Other"}));

    }

    private void EmployeeRegister(Employee employee){
        RetroInterface retroInterface= Retrofi.initretro().create(RetroInterface.class);
        Call<Employee> call=retroInterface.addEmployee(employee);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
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


    public void showmyinformation(View view){
        expandablemycontent = (ExpandableLayout) findViewById(R.id.mycontent);
        expandablemycontent.toggle();
    }

    public void showjobinformation(View view){
        expandableinterncontent = findViewById(R.id.myjobcontent);
        expandableinterncontent.toggle();
    }
}