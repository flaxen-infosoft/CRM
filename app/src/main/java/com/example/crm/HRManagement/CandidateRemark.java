package com.example.crm.HRManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crm.Model.Candidate;
import com.example.crm.PaySlipFormActivity;
import com.example.crm.R;
import com.example.crm.Retro.RetroInterface;
import com.example.crm.Retro.Retrofi;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidateRemark extends AppCompatActivity {

    Button updatebt, dateofinterviewbt;
    EditText designation;
    Spinner spin_department, spin_status;
    RadioButton job, intern, laptopyes, laptopno;
    String startdate, appliedfor, havelaptop;
    ExpandableLayout expandablemycontent, expandableinterncontent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_remark);
        dateofinterviewbt = findViewById(R.id.candidate_remark_dateofinterview);
        updatebt = findViewById(R.id.candidate_remark_resume);
        spin_department = findViewById(R.id.department);
        spin_status = findViewById(R.id.status);
        designation=findViewById(R.id.candidate_designation);
        laptopyes = findViewById(R.id.yes);
        laptopno = findViewById(R.id.no);
        job = findViewById(R.id.job);
        intern = findViewById(R.id.intern);
        bundle = getIntent().getExtras();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Department, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_department.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Status, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_status.setAdapter(adapter1);
        dateofinterviewbt.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CandidateRemark.this, (datePicker, i, i1, i2) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                startdate = sdf.format(calendar.getTime());
                dateofinterviewbt.setText(startdate);
            }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("start date");
            datePickerDialog.show();
        });
        updatebt.setOnClickListener(v -> {
            if (job.isChecked()) {
                appliedfor = "job";
            } else {
                appliedfor = "internship";
            }
            if (laptopyes.isChecked()) {
                havelaptop = "yes";
            } else {
                havelaptop = "no";
            }
            Candidate candidate= new Candidate();
            candidate.setName(bundle.getString("name"));
            candidate.setAddress(bundle.getString("address"));
            candidate.setAltphone(bundle.getString("altphone"));
            candidate.setState(bundle.getString("state"));
            candidate.setCity(bundle.getString("city"));
            candidate.setPhone(bundle.getString("phone"));
            candidate.setSource(bundle.getString("source"));
            candidate.setPid(bundle.getString("personalemail"));
            candidate.setOid(bundle.getString("officialemail"));
            candidate.setDesignation(designation.getText().toString());
            candidate.setHave_laptop(havelaptop);
            candidate.setApplied_for(appliedfor);
            candidate.setDateof_interview(startdate);
            System.out.println("candidate = " + candidate);
            CandidateRegister(candidate);

        });

    }

    private void CandidateRegister(Candidate candidate) {
        RetroInterface retroInterface = Retrofi.initretro().create(RetroInterface.class);
        Call<Candidate> call = retroInterface.addCandidate(candidate);
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

    public void showmyinformation(View view) {
        expandablemycontent = (ExpandableLayout) findViewById(R.id.mycontent);
        expandablemycontent.toggle();
    }

    public void showjobinformation(View view) {
        expandableinterncontent = findViewById(R.id.myjobcontent);
        expandableinterncontent.toggle();
    }
}