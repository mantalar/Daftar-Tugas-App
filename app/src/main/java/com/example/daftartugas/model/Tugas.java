package com.example.daftartugas.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Tugas implements Serializable {
    private int id;
    private String tugas;

    public Tugas(int id, String tugas) {
        this.id = id;
        this.tugas = tugas;
    }

    public Tugas(String tugas) {
        this.tugas = tugas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTugas() {
        return tugas;
    }

    public void setTugas(String tugas) {
        this.tugas = tugas;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%3d - %s", getId(), getTugas());
    }
}
