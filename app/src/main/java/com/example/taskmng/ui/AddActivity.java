package com.example.taskmng.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmng.R;
import com.example.taskmng.model.Task;
import com.example.taskmng.network.ApiAdapter;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements View.OnClickListener, Callback<Task> {
    public static final int OK = 1;

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.link)
    EditText link;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.importancia)
    EditText importancia;
    @BindView(R.id.fecha)
    EditText fecha;
    @BindView(R.id.imagen)
    EditText image;

    @BindView(R.id.accept)
    Button accept;
    @BindView(R.id.cancel)
    Button cancel;
    //EditText name, link, emailSite;
    //Button accept, cancel;

    ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButterKnife.bind(this);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String n, l, i, f, imp, d;
        Task s;

        if (v == accept) {
            n = name.getText().toString();
            l = link.getText().toString();
            i = image.getText().toString();
            d = desc.getText().toString();
            f = fecha.getText().toString();
            imp = importancia.getText().toString();
            if (n.isEmpty() || l.isEmpty())
                Toast.makeText(this, "Please, fill the name and the link", Toast.LENGTH_SHORT).show();
            else {
                s = new Task(n, d, imp, l, i, f);
                connection(s);
            }
        }
        if (v == cancel)
            finish();
    }

    private void connection(Task s) {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Connecting . . .");
        progreso.setCancelable(false);
        progreso.show();

        Call<Task> call = ApiAdapter.getInstance().createSite(s);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Task> call, Response<Task> response) {
        progreso.dismiss();
        if (response.isSuccessful()) {
            Task task = response.body();
            Intent i = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putInt("id", task.getId());
            mBundle.putString("name", task.getName());
            mBundle.putString("desc", task.getDesc());
            mBundle.putString("imp", task.getImp());
            mBundle.putString("link", task.getDesc());
            mBundle.putString("img", task.getImg());
            mBundle.putString("fecha", task.getDate());
            i.putExtras(mBundle);
            setResult(OK, i);
            finish();
            showMessage("Added task ok");
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Download error: " + response.code());
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
    public void onFailure(Call<Task> call, Throwable t) {
        progreso.dismiss();
        if (t != null)
            showMessage("Failure in the communication\n" + t.getMessage());
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
