package com.example.crm.EmployeeManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crm.HRManagement.CandidateRegistration;
import com.example.crm.HRManagement.CandidateRemark;
import com.example.crm.Model.Employee;
import com.example.crm.R;
import com.example.crm.Retro.RetroInterface;
import com.example.crm.Retro.Retrofi;
import com.example.crm.citystate.Cities;
import com.example.crm.citystate.Rinterface;
import com.example.crm.citystate.object;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeRegistrationActvity extends AppCompatActivity {

    Spinner stateSpin, citySpin, genderSpin, statusspin;
    List<String> stateList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();
    Button register, dojbt;
    RadioButton job, intern;
    EditText empname, empdesignation, phoneno, offid, personalid, password;
    ExpandableLayout expandablemycontent, expandableinterncontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_registration_actvity);
        empname = findViewById(R.id.empregname);
        empdesignation = findViewById(R.id.empdesignation);
        phoneno = findViewById(R.id.empphoneno);
        offid = findViewById(R.id.candidate_personal_email);
        personalid = findViewById(R.id.candidate_official_email);
        password = findViewById(R.id.emppassword);
        statusspin=findViewById(R.id.employee_status);
        stateSpin = findViewById(R.id.state);
        citySpin = findViewById(R.id.city);
        register = findViewById(R.id.btn_register);
        job = findViewById(R.id.job);
        intern = findViewById(R.id.intern);
        genderSpin = findViewById(R.id.gender);
        dojbt = findViewById(R.id.employee_dojbt);
        dojbt.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeRegistrationActvity.this, (datePicker, i, i1, i2) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                String startdate = sdf.format(calendar.getTime());
                dojbt.setText(startdate);
            }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("start date");
            datePickerDialog.show();
        });
        register.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeRegistrationActvity.this, EmployeeRegisterSecondActivity.class);
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

    private void Check() {
        String canname = empname.getText().toString();
        String canphone = phoneno.getText().toString();
        String canpersonalemail = personalid.getText().toString();
        String canpassword = password.getText().toString();
        String canofficialemail = offid.getText().toString();
        String candesignation = empdesignation.getText().toString();
        String canstate = stateSpin.getSelectedItem().toString();
        String cancity = citySpin.getSelectedItem().toString();
        if (canphone.length() != 10) {
            phoneno.setError("Please Enter Valid Phone Number ");
            phoneno.requestFocus();
        } else if (canname.isEmpty()) {
            empname.setError("Please Enter  Name");
            empname.requestFocus();
        } else if (!canpersonalemail.contains("@")) {
            personalid.setError("Please Enter a Valid Email Address");
            personalid.requestFocus();
        } else if (candesignation.isEmpty()) {
            empdesignation.setError("Please Enter Designation");
            empdesignation.requestFocus();

        }  else if (!canofficialemail.contains("@")) {
            offid.setError("Please Enter a Valid Email Address");
            offid.requestFocus();
        } else if (canpassword.isEmpty()) {
            password.setError("Please Enter password");
            password.requestFocus();
        } else if (canstate.contains("Select State")) {
            Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
        } else if (cancity.contains("Select City")) {
            Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("personalemail", canpersonalemail);
            bundle.putString("officialemail", canofficialemail);
            bundle.putString("address", candesignation);
            bundle.putString("password", canpassword);
            bundle.putString("phone", canphone);
            bundle.putString("name", canname);
            bundle.putString("state", canstate);
            bundle.putString("city", cancity);
            Intent intent = new Intent(EmployeeRegistrationActvity.this, EmployeeRegisterSecondActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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