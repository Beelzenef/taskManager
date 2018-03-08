package com.example.taskmng;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.taskmng.adapter.ClickListener;
import com.example.taskmng.adapter.RecyclerTouchListener;
import com.example.taskmng.adapter.TasksAdapter;
import com.example.taskmng.model.Task;
import com.example.taskmng.network.ApiAdapter;
import com.example.taskmng.ui.AddActivity;
import com.example.taskmng.ui.EmailActivity;
import com.example.taskmng.ui.UpdateActivity;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int ADD_CODE = 100;
    public static final int UPDATE_CODE = 200;
    public static final int OK = 1;
    public static final String MAIL = "mail";

    @BindView(R.id.floatingActionButton)
    FloatingActionButton fab;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    int positionClicked;
    private TasksAdapter adapter;
    private ArrayList<Task> tasks;

    ProgressDialog progreso;
    //ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        //fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
        //Initialize RecyclerView
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new TasksAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //manage click
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showPopup(view, position);
            }
            @Override
            public void onLongClick(View view, int position) {
                Intent emailIntent = new Intent(getApplicationContext(), EmailActivity.class);
                emailIntent.putExtra(MAIL, adapter.getAt(position).getImportancia());
                startActivity(emailIntent);
            }
        }));

        downloadSites();
    }

    private void downloadSites() {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando...");
        progreso.setCancelable(false);
        progreso.show();

        Call<ArrayList<Task>> call = ApiAdapter.getInstance().getTasks();
        call.enqueue(new Callback<ArrayList<Task>>() {
            @Override
            public void onResponse(Call<ArrayList<Task>> call, Response<ArrayList<Task>> response) {
                progreso.dismiss();
                if (response.isSuccessful()) {
                    tasks = response.body();
                    adapter.setSites(response.body());
                    showMessage("Descarga correcta");
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("Error de descarga: " + response.code());
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
            public void onFailure(Call<ArrayList<Task>> call, Throwable t) {
                progreso.dismiss();
                if (t != null)
                    showMessage("Fallo en la comunicación:\n" + t.getMessage());
            }
        });
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando...");
        progreso.setCancelable(false);
        progreso.show();
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent i = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(i, ADD_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task task = new Task();

        if (requestCode == ADD_CODE)
            if (resultCode == OK) {
                task.setId(data.getIntExtra("id", 1));
                task.setNombre(data.getStringExtra("nombre"));
                task.setDesc(data.getStringExtra("desc"));
                task.setImportancia(data.getStringExtra("importancia"));
                task.setFecha(data.getStringExtra("fecha"));
                task.setEnlace(data.getStringExtra("enlace"));
                task.setImagen(data.getStringExtra("imagen"));

                adapter.add(task);
            }

        if (requestCode == UPDATE_CODE)
            if (resultCode == OK) {
                task.setId(data.getIntExtra("id", 1));
                task.setNombre(data.getStringExtra("nombre"));
                task.setDesc(data.getStringExtra("desc"));
                task.setImportancia(data.getStringExtra("importancia"));
                task.setFecha(data.getStringExtra("fecha"));
                task.setEnlace(data.getStringExtra("enlace"));
                task.setImagen(data.getStringExtra("imagen"));
                adapter.modifyAt(task, positionClicked);
            }
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_change, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify_site:
                        modify(adapter.getAt(position));
                        positionClicked = position;
                        return true;
                    case R.id.delete_site:
                        confirm(adapter.getAt(position).getId(), adapter.getAt(position).getNombre(), position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Show the menu
        popup.show();
    }

    private void modify(Task s) {
        Intent i = new Intent(MainActivity.this, UpdateActivity.class);
        i.putExtra("task", s);
        startActivityForResult(i, UPDATE_CODE);
    }

    private void confirm(final int idSite, String name, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(name + "\n¿Seguro que quieres borrar?")
                .setTitle("Borrando tarea")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        connection(idSite, position);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void connection(int idTask, final int position) {
        //Call<ResponseBody> call = ApiAdapter.getInstance().deleteTask(position);
        Call<ResponseBody> call = ApiAdapter.getInstance().deleteTask(idTask);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando...");
        progreso.setCancelable(false);
        progreso.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progreso.dismiss();
                if (response.isSuccessful()) {
                    adapter.removeAt(position);
                    showMessage("Tarea eliminada correctamente");
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("Error eliminando una tarea: " + response.code());
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
                    showMessage("Fallo en la comunicación\n" + t.getMessage());
            }
        });
    }
}
