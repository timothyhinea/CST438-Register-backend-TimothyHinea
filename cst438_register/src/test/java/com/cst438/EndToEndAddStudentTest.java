package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 */

@SpringBootTest
public class EndToEndAddStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = 
			"C:\\Users\\Andrew\\Desktop\\IDE\\chromedriver.exe";

	public static final String URL = "https://cst438register-fe-hinea.herokuapp.com/";

	public static final String TEST_USER_EMAIL = "testTwo@csumb.edu";

	public static final String TEST_USER_NAME = "testTwo";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Test
	public void addCourseTest() throws Exception {

		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_USER_EMAIL);
			System.err.println(x);
			if (x != null) {
				//enrollments have Forign key of student id --- need to delete all enrollments for test student
				List<Enrollment> enrollment = enrollmentRepository.findAllStudentEnrollments(x.getEmail());
				for(Enrollment e: enrollment) {
					enrollmentRepository.delete(e);
				}
				
				studentRepository.delete(x);
				System.err.println("after delete");
			}
		} while (x != null);

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);


			// Locate and click "Get Students" button
			driver.findElement(By.xpath("//a[last()]")).click();
			Thread.sleep(SLEEP_DURATION);

			// Locate and click "Add Student" button
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);

			// Enter email, name, and click "Add"
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);

			driver.findElement(By.xpath("//button[span='Add']")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify that new student shows in student list and repository.
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			//Verify student shows up in repository
			assertNotNull(student, "Course enrollment not found in database.");
			WebElement we = driver.findElement(By.xpath("//div[@data-field='email' and @data-value='" + student.getEmail() + "']"));
			//Verify student shows up in student list
			assertNotNull(we, "Added course does not show in schedule.");

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			Student e = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (e != null)
				studentRepository.delete(e);
			driver.quit();
		}

	}
}
