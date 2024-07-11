package com.example.demo.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entities.Student;

public interface StudentRespository extends JpaRepository<Student, Integer> {

}
