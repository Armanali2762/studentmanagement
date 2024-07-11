package com.example.demo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentMarks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String math;
	private String science;
	private String english;
	private String hindi;
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
}
