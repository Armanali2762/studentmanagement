package com.example.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "query_condition")
public class QueryCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Long pk;

    @Column(name = "query_list_pk", nullable = false)
    private Long queryListPk;

    @Column(name = "condition", nullable = false)
    private String condition;

    @Column(name = "active", columnDefinition = "TINYINT(1)")
    private Boolean active = true;
}
