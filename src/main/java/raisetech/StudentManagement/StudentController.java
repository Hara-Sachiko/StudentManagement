package raisetech.StudentManagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class StudentController {

  private final StudentRepository repository;

  public StudentController(StudentRepository repository) {
    this.repository = repository;
  }

  // -----------------------
  // students 全件取得
  // -----------------------
  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return repository.findAllStudents();
  }

  // -----------------------
  // students 正規表現検索
  // -----------------------
  @GetMapping("/students/search")
  public List<Student> searchStudents(@RequestParam(required = false) String namePattern) {
    if (namePattern == null || namePattern.isEmpty()) {
      return repository.findAllStudents(); // パラメータなしなら全件取得
    }
    return repository.findStudentsByNamePattern(namePattern);
  }

  // -----------------------
  // courses 全件取得
  // -----------------------
  @GetMapping("/courses")
  public List<StudentCourse> getAllCourses() {
    return repository.findAllCourses();
  }

  // -----------------------
  // courses 正規表現検索
  // -----------------------
  @GetMapping("/courses/search")
  public List<StudentCourse> searchCourses(@RequestParam String coursePattern) {
    return repository.findCoursesByNamePattern(coursePattern);
  }
}
