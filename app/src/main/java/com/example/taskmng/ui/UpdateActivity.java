package com.example.taskmng.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmng.R;
import com.example.taskmng.model.Task;
import com.example.taskmng.network.ApiAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, Callback<Task> {
    public static final int OK = 1;

    @BindView(R.id.idSite)
    TextView idSite;
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
    @BindView(R.id.imgV_Descargas)
    ImageView imgV_Descargas;

    @BindView(R.id.accept)
    Button accept;
    @BindView(R.id.cancel)
    Button cancel;

    ProgressDialog progreso;
    Task s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ButterKnife.bind(this);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent i = getIntent();
        s = (Task) i.getSerializableExtra("task");
        idSite.setText(Integer.toString(s.getId()));
        name.setText(s.getNombre());
        link.setText(s.getEnlace());
        if (!TextUtils.isEmpty(s.getImagen())) {
            image.setText(s.getImagen());
            picasso(s.getImagen());
        }
        desc.setText(s.getDesc());
        importancia.setText(s.getImportancia());
        fecha.setText(s.getFecha());


    }

    @Override
    public void onClick(View v) {
        String n, l, i, f, imp, d;

        if (v == accept) {
            n = name.getText().toString();
            l = link.getText().toString();
            i = image.getText().toString();
            d = desc.getText().toString();
            f = fecha.getText().toString();
            imp = importancia.getText().toString();
            if (n.isEmpty() || l.isEmpty())
                Toast.makeText(this, "Rellena los campos vacíos", Toast.LENGTH_SHORT).show();
            else {
                s = new Task(n, d, imp, f, l, i);
                connection(s);
            }
        }
        if (v == cancel)
            finish();
    }

    private void connection(Task s) {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando...");
        progreso.setCancelable(false);
        progreso.show();

        Call<Task> call = ApiAdapter.getInstance().updateTask(s, s.getId());
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
            mBundle.putString("nombre", task.getNombre());
            mBundle.putString("desc", task.getDesc());
            mBundle.putString("importancia", task.getImportancia());
            mBundle.putString("fecha", task.getFecha());
            mBundle.putString("enlace", task.getEnlace());
            mBundle.putString("imagen", task.getImagen());
            i.putExtras(mBundle);
            setResult(OK, i);
            finish();
            showMessage("Modificación correcta");
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Error en la descarga: " + response.code());
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
            showMessage("Fallo en la comunicación\n" + t.getMessage());
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void picasso(String link) {
        Picasso.with(getApplicationContext()).load(link).placeholder(R.drawable.placeholder).error(R.drawable.placeholder_error)
                .resize(300, 300).into(imgV_Descargas);
    }
}