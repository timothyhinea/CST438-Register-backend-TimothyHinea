package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	
	public Student findByEmail(String email);
	
	@Query("select email from Student e")
	Student selectAll();
	
	@SuppressWarnings("unchecked")
	Student save(Student e);
}
