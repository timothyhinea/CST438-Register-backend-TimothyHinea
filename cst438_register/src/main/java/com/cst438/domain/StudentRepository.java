package com.cst438.domain;

//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	
	public Student findByEmail(String email);
	
	@Query("select email from Student e")
	Student selectAll();
	
	@SuppressWarnings("unchecked")
	Student save(Student e);
	
//	//@Modifying
//	@Query("update Student e set e.status_code = 1 where e.email =:email")
//	void addStatusCode(String email);
}
