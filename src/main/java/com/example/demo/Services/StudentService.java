package com.example.demo.Services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.StudentRespository;
import com.example.demo.Entities.Student;

@Service

public class StudentService {

    // This is for inject the object of userRespository
    @Autowired
    private StudentRespository studentRespository;

    // This is for insert student in database
    @Transactional
    public int saveUser(Student user) {

        if (user != null) {
            this.studentRespository.save(user);
            return user.getId();
        } else {
            throw new IllegalArgumentException("user is null, please enter user with correct value");
        }

    }

    // This is for delete student
    public Student deleteUser(int id) {
        if (id != 0) {
            Optional<Student> optionaluser = this.studentRespository.findById(id);
            if (optionaluser.isPresent()) {
                this.studentRespository.deleteById(id);
                return optionaluser.get();
            } else {
                throw new EntityNotFoundException("Invalid user id, please enter correct user id");
            }
        } else {
            throw new IllegalArgumentException("Id should not be zero, please enter correct id");
        }
    }

    // This is for fetch students
    public Page<Student> getStudents(Integer offset, Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(offset, pageSize);
            return this.studentRespository.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch students: " + e.getMessage(), e);
        }
    }

    // This is for delete student
    public boolean deleteStudent(int id) {
        if (id != 0) {
            this.studentRespository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
