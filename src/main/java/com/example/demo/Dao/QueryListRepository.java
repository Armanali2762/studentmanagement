package com.example.demo.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entities.QueryList;

public interface QueryListRepository extends JpaRepository<QueryList, Long> {

}
