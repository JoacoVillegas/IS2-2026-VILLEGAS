package com.example.colegio.repository;

import com.example.colegio.entity.Colegio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColegioRepository extends JpaRepository<Colegio, Long> {
}
