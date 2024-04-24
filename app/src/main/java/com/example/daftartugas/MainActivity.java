package com.example.daftartugas;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daftartugas.adapter.TugasAdapter;
import com.example.daftartugas.model.Tugas;
import com.example.daftartugas.utils.TugasDB;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Tugas> mList = new ArrayList<>();
    private TugasAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        mAdapter = new TugasAdapter(mList, this);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(this::onTugasLongClick);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> createDialog(null).show());
    }

    private BottomSheetDialog createDialog(Bundle bundle) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this)
                .inflate(R.layout.new_tugas_layout, null, false);

        EditText etTugas = view.findViewById(R.id.et_tugas);
        Button btAdd = view.findViewById(R.id.bt_add);

        boolean isUpdate = false;
        if (bundle != null) {
            etTugas.setText(bundle.getString("tugas"));
            isUpdate = true;
            btAdd.setText("Update");
        }

        final boolean finalIsUpdate = isUpdate;
        btAdd.setOnClickListener(v -> {
            if (etTugas.getText().toString().isEmpty()) {
                return;
            }

            try (TugasDB db = new TugasDB(this)) {
                if (finalIsUpdate) {
                    //TODO update
                    int position = bundle.getInt("position");
                    Tugas tugas = new Tugas(bundle.getInt("id"), etTugas.getText().toString());
                    db.update(tugas);
                    mList.set(position, tugas);
                } else {
                    //TODO insert
                    int id = (int) db.insert(new Tugas(etTugas.getText().toString()));
                    mList.add(new Tugas(id, etTugas.getText().toString()));
                }
            }

            mAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        return dialog;
    }

    private boolean onTugasLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence[] items = {"Edit", "Delete"};
        int[] checked = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Opsi")
                .setSingleChoiceItems(items, checked[0], (dialog, which) -> checked[0] = which)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", (dialog, which) -> {

                    Tugas tugas = mList.get(i);

                    switch (checked[0]) {
                        case 0: //TODO edit
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", i);
                            bundle.putInt("id", tugas.getId());
                            bundle.putString("tugas", tugas.getTugas());

                            createDialog(bundle).show();
                            break;
                        case 1: //TODO delete
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm")
                                    .setMessage(String.format("Delete tugas %s?", mList.get(i).getTugas()))
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Yes", (dialog1, which1) -> {
                                        try (TugasDB db = new TugasDB(this)) {
                                            db.delete(tugas.getId());
                                            mList.remove(tugas);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .show();
                    }
                })
                .show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mi_about) {
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage(R.string.msg_about)
                    .setPositiveButton("Ok", null)
                    .show();
        } else if (item.getItemId() == R.id.mi_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Close App?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try (TugasDB db = new TugasDB(this)) {
            mList.clear();
            mList.addAll(db.getAllTugas());
            mAdapter.notifyDataSetChanged();
        }
    }
}