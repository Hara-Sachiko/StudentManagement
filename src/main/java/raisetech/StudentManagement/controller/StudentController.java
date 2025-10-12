package raisetech.StudentManagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.etc.StudentWithCourse;

@RestController
public class StudentController {

  private final StudentService service;

  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return service.getAllStudents();
  }

  @GetMapping("/students/search")
  public List<Student> searchStudents(@RequestParam(required = false) String namePattern) {
    return service.searchStudents(namePattern);
  }

  @GetMapping("/courses")
  public List<StudentCourse> getAllCourses() {
    return service.getAllCourses();
  }

  @GetMapping("/courses/search")
  public List<StudentCourse> searchCourses(@RequestParam(required = false) String coursePattern) {
    return service.searchCourses(coursePattern);
  }

  @GetMapping("/students/30s")
  public List<Student> getStudentsIn30s() {
    return service.getStudentsIn30s();
  }

  @GetMapping("/students/java")
  public List<StudentWithCourse> getStudentsInJavaCourse() {
    return service.getStudentsInJavaCourse("Javaコース"); // ←固定値
  }
}