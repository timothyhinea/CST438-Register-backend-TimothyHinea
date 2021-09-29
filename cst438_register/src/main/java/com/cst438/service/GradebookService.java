package com.cst438.service;

public class GradebookService {

	public void enrollStudent(String student_email, String student_name, int course_id) {
		//GradebookServiceREST gradebookServiceREST = new GradebookServiceREST();
		//gradebookServiceREST.enrollStudent(student_email, student_name, course_id);
		
		GradebookServiceMQ gradeBookServiceMQ = new GradebookServiceMQ();
		gradeBookServiceMQ.enrollStudent(student_email, student_name, course_id);
	}
	
}
