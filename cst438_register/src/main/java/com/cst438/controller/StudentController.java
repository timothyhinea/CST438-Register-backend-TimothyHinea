package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.AdminRepository;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
//import com.cst438.service.GradebookService;
import com.cst438.domain.StudentDTO;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class StudentController {
	
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
//	@Autowired
//	GradebookService gradebookService;
	
	
	/*
	 * get student by email schedule for student.
	 */
	@GetMapping("/student")
	public StudentDTO getStudent( @RequestParam("email") String email, @AuthenticationPrincipal OAuth2User principal){
		Student student = null;
		if(isAdmin(principal.getAttribute("email")))
			student = studentRepository.findByEmail(email);
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Permissions");
		
		if (student != null) {
			StudentDTO sched = createStudentDTO(student);
			return sched;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student not found. " );
		}
	}

	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO, @AuthenticationPrincipal OAuth2User principal) { 
		Student student = null;
		if(isAdmin(principal.getAttribute("email")))
			student = studentRepository.findByEmail(studentDTO.email);
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Permissions");
		if(student == null) {
			student = new Student();
			student.setEmail(studentDTO.email);
			student.setName(studentDTO.name);
			Student student_with_key = studentRepository.save(student);
			studentDTO.student_id = student_with_key.getStudent_id();
			return studentDTO;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/student/{email}")
	@Transactional
	public StudentDTO addAndRemoveHold(  @PathVariable String email, @RequestParam("status_code") int status_code, @AuthenticationPrincipal OAuth2User principal  ) { 
		Student student = null;
		if(isAdmin(principal.getAttribute("email"))) {
			student = studentRepository.findByEmail(email);
			student.setStatusCode(status_code);
		}
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Permissions");
		
		return  createStudentDTO(student);

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
	private boolean isAdmin(String email) {
		if(adminRepository.findByEmail(email) == null)
			return false;
		else
			return true;
	}
	
}
