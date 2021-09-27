package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.StudentDTO;
import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Student;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		EnrollmentDTO smsg = new EnrollmentDTO(student_email, student_name, course_id); 
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), smsg);
		
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(CourseDTOG grades) {
		Course course = courseRepository.findByCourse_id(grades.course_id);
		for(CourseDTOG.GradeDTO grade : grades.grades) {
			Student student = studentRepository.findByEmail(grade.student_email);
			enrollmentRepository.upDateEnrollmentGrade(grade.grade, student, course );
		
	}
	}
	
	

}
