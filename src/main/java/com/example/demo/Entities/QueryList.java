package com.example.demo.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "query_list")
public class QueryList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Long pk;

    @Column(name = "query", length = 8000)
    private String query;

    @Column(name = "count_column", length = 50)
    private String countColumn;

    @Column(name = "action", columnDefinition = "tinyint default 0")
    private Integer action;

    @Column(name = "delete_column", length = 500)
    private String deleteColumn;

    @Column(name = "checkbox", columnDefinition = "tinyint default 0")
    private Boolean checkbox;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "default_order_by_column", length = 255)
    private String defaultOrderByColumn;

}
