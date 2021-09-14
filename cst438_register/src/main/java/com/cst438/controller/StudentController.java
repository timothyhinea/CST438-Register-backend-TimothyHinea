package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.cst438.domain.StudentDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	
	/*
	 * get student by email schedule for student.
	 */
	@GetMapping("/student")
	public StudentDTO getStudent( @RequestParam("email") String email){
		System.out.print(email);
		
		Student student = studentRepository.findByEmail(email);
		System.out.print(student);
		if (student != null) {
			StudentDTO sched = createStudentDTO(student);
			return sched;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}

	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO  ) { 
		boolean flag = false;
		Iterable<Student> existingStudents = studentRepository.findAll();
		for(Student s: existingStudents) 
			if(s.getEmail().equals(studentDTO.email))
				flag = true;
		
		if(!flag) {
			Student result = new Student();
			result.setName(studentDTO.name);
			result.setEmail(studentDTO.email);
			studentRepository.save(result);
			return studentDTO;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/* 
	 * helper method to transform course, enrollment, student entities into 
	 * a an instance of ScheduleDTO to return to front end.
	 * This makes the front end less dependent on the details of the database.
	 */
	
	private StudentDTO createStudentDTO(Student s)
	{
		StudentDTO result = new StudentDTO();
		result.student_id = s.getStudent_id();
		result.email = s.getEmail();
		result.status_code = s.getStatusCode();
		result.status = s.getStatus();
		result.name = s.getName();
		return result;
	}
	
}
