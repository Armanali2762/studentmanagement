package com.example.demo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull(message = "name is null")
    private String name;
    @NotNull(message = "gender is null")
    private String gender;
    private String course;
    @NotNull(message = "country is null")
    private String country;
    @NotNull(message = "country is null")
    private String state;
    @NotNull(message = "state is null")
    private String city;
    @NotNull(message = "address is null")
    private String address;
}
