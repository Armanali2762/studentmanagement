package com.example.demo.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entities.StudentMarks;

public interface StudentMarksRespository extends JpaRepository<StudentMarks, Integer> {

}
