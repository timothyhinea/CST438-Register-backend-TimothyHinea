package com.cst438.domain;

//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

public interface AdminRepository extends CrudRepository <Admin, Integer> {
	
	public Admin findByEmail(String email);
	
	@Query("select email from Admin e")
	Admin selectAll();
	
	@Query("select s from Admin s where s.admin_id=:admin_id")
	public Admin findById(@Param("admin_id") int admin_id);
	
	public Admin save(Admin e);
	
//	//@Modifying
//	@Query("update Student e set e.status_code = 1 where e.email =:email")
//	void addStatusCode(String email);
}
