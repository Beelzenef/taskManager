package com.example.taskmng.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmng.MainActivity;
import com.example.taskmng.R;
import com.example.taskmng.model.Email;
import com.example.taskmng.network.ApiAdapter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener, Callback<ResponseBody> {
    public static final int OK = 1;
    public static final String MAIL = MainActivity.MAIL;

    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.subject)
    EditText subject;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.accept)
    Button accept;
    @BindView(R.id.cancel)
    Button cancel;
    //EditText from, password, to, subject, message;
    //Button accept, cancel;
    ProgressDialog progreso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ButterKnife.bind(this);
        //from = (EditText) findViewById(R.id.from);
        //password = (EditText) findViewById(R.id.password);
        //to = (EditText) findViewById(R.id.to);
        //subject = (EditText) findViewById(R.id.subject);
        //message = (EditText) findViewById(R.id.message);
        //accept = (Button) findViewById(R.id.accept);
        //cancel = (Button) findViewById(R.id.cancel);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent i = getIntent();
        to.setText(i.getStringExtra(MAIL));
    }

    @Override
    public void onClick(View v) {

        if (v == accept) {
            String t = to.getText().toString();
            String s = subject.getText().toString();
            String m = message.getText().toString();

            //if (t.isEmpty() || s.isEmpty() || m.isEmpty()) {
            //    showMessage("Please, fill to, subject and message");
            //} else {
                Email email = new Email(t, s, m);
                connection(email);
            //}
        }

        if (v == cancel) {
            finish();
        }
    }

    private void connection(Email e) {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Connecting . . .");
        progreso.setCancelable(false);
        progreso.show();

        Call<ResponseBody> call = ApiAdapter.getInstance().sendEmail(e);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        progreso.dismiss();
        if (response.isSuccessful()) {
            Intent i = new Intent();
            setResult(OK, i);
            finish();
            showMessage("Email sent ok");
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Error sending the mail: " + response.code());
            if (response.body() != null)
                message.append("\n" + response.body());
            if (response.errorBody() != null)
                try {
                    message.append("\n" + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            showMessage(message.toString());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        progreso.dismiss();
        if (t != null)
            showMessage("Failure in the communication\n" + t.getMessage());
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}