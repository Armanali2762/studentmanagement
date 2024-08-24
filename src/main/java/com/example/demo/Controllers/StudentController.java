package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Dao.StudentMarksRespository;
import com.example.demo.Dao.StudentRespository;
import com.example.demo.Entities.Student;
import com.example.demo.Entities.StudentMarks;

@Controller
public class StudentController {

	@Autowired
	private StudentRespository studentRespository;
	@Autowired
	private com.example.demo.Services.StudentService StudentService;
	@Autowired
	private StudentMarksRespository studentMarksRespository;

	// @PostConstruct
	// public void initDB() {
	// List<Student> students = IntStream.rangeClosed(1, 200)
	// .mapToObj(i -> new Student(new Random().nextInt(100), "student" + i, "male",
	// "Computer Science", "China", "Beijing", "Chaoyang", "India"))
	// .collect(Collectors.toList());
	// this.studentRespository.saveAll(students);
	// }

	@GetMapping("/addstudent")
	public String showAddStudentPage() {
		return "addstudent";
	}

	@GetMapping("/student")
	public String getStudent() {
		return "index";
	}

	@PostMapping("/savestudent")
	public String processSignupForm(@ModelAttribute("Student") Student student,
			HttpServletRequest request, Model model, @RequestParam("countrow") Integer count) {
		try {
			this.StudentService.saveUser(student);
			if (count != 0) {
				ArrayList<StudentMarks> studentMarksList = new ArrayList<>();
				for (int i = 0; i < count; i++) {
					studentMarksList.add(new StudentMarks(
							new Random().nextInt(10),
							request.getParameter("math" + i),
							request.getParameter("science" + i),
							request.getParameter("english" + i),
							request.getParameter("hindi" + i),
							student));
				}
				System.out.println(studentMarksList);
				this.studentMarksRespository.saveAll(studentMarksList);
			}
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", "Failed to process registration: " + e.getMessage());
			e.printStackTrace();
		}
		return "index";
	}

	@ResponseBody
	@GetMapping("/getstudents")
	public Map<String, Object> getStudents(
			@RequestParam(name = "draw", defaultValue = "0") int draw,
			@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length) {
		try {
			Pageable pageable = PageRequest.of(start / length, length);

			Page<Student> studentsPage = studentRespository.findAll(pageable);
			List<Student> studentsList = studentsPage.getContent();

			List<List<Object>> data = studentsList.stream()
					.map(student -> {
						List<Object> rowData = new ArrayList<>();
						rowData.add(student.getId());
						rowData.add(student.getName());
						rowData.add(student.getGender());
						rowData.add(student.getCourse());
						rowData.add(student.getCountry());
						rowData.add(student.getState());
						rowData.add(student.getCity());
						rowData.add(student.getAddress());
						String updateLink = "<button class='btn btn-primary' id='openEditButton' onclick='updateStudent("
								+ student.getId() + ")'>Update</button>";
						String deleteLink = "<button class='btn btn-danger' onclick='deleteStudent(" + student.getId()
								+ ")'>Delete</button>";

						rowData.add(updateLink);
						rowData.add(deleteLink);

						return rowData;
					})
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("draw", draw);
			response.put("recordsTotal", studentsPage.getTotalElements());
			response.put("recordsFiltered", studentsPage.getTotalElements());
			response.put("data", data);

			return response;
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch students: " + e.getMessage(), e);
		}
	}

	@DeleteMapping("/deletestudent/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable("id") int id) {
		try {
			if (id != 0) {
				this.StudentService.deleteStudent(id);
				return ResponseEntity.ok("student is deleted successfully");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id is zero");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Internal server error");
		}
	}

	@ResponseBody
	@GetMapping("/editStudent/{id}")
	public ResponseEntity<?> getStudentForEdit(@PathVariable("id") int id, Model model) {
		try {
			Optional<Student> optionalStudent = this.studentRespository.findById(id);
			if (optionalStudent.isPresent()) {
				Student student = optionalStudent.get();
				System.out.println(student + "--------------------------------");
				model.addAttribute("student", student);
				return ResponseEntity.ok(student);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("studentNotFound");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
		}
	}

	@PostMapping("/updateStudent/{id}")
	public String updateStudent(@PathVariable("id") int id, @ModelAttribute("Student") Student student) {
		System.out.println(student + "_----------------------" + id);

		Optional<Student> optionalStudent = this.studentRespository.findById(id);
		if (optionalStudent.isPresent()) {
			Student existingStudent = optionalStudent.get();
			existingStudent.setName(student.getName());
			existingStudent.setGender(student.getGender());
			existingStudent.setCourse(student.getCourse());
			existingStudent.setCountry(student.getCountry());
			existingStudent.setState(student.getState());
			existingStudent.setCity(student.getCity());
			existingStudent.setAddress(student.getAddress());
			this.studentRespository.save(existingStudent);
			return "redirect:/student";
		} else {
			return "redirect:/error";
		}

	}

}
