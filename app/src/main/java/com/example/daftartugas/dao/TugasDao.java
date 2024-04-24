package com.example.daftartugas.dao;

import com.example.daftartugas.model.Tugas;

import java.util.List;

public interface TugasDao {
    long insert(Tugas tugas);
    void update(Tugas tugas);
    void delete(int id);

    Tugas getATugasById(int id);
    List<Tugas> getAllTugas();
}
