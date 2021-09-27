package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	String gradebook_url = "http://localhost:8081";
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}


	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		EnrollmentDTO smsg = new EnrollmentDTO(student_email, student_name, course_id);
		System.out.println("Sending http message: "+smsg);
		ResponseEntity<EnrollmentDTO> response = restTemplate.postForEntity(
				"http://localhost:8081/enrollment",   // URL
				smsg,                              // data to send
				EnrollmentDTO.class);              // return data type
		
		HttpStatus rc = response.getStatusCode();
		System.out.println("HttpStatus: "+rc);
		EnrollmentDTO returnObject = response.getBody();
		System.out.println(returnObject);
		
	}

}
