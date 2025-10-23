package com.example.cursoudemy.libraryapi.repository;

import com.example.cursoudemy.libraryapi.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
