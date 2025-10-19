package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.etc.StudentWithCourse;

@Service
public class StudentService {

  private final StudentRepository repository;

  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> getAllStudents() {
    return repository.findAllStudents();
  }

  public List<StudentCourse> getAllCourses() {
    return repository.findAllCourses();
  }

  public List<Student> searchStudents(String namePattern) {
    if (namePattern == null || namePattern.isEmpty()) {
      return repository.findAllStudents();
    }
    return repository.findStudentsByNamePattern(namePattern);
  }

  public List<StudentCourse> searchCourses(String coursePattern) {
    if (coursePattern == null || coursePattern.isEmpty()) {
      return repository.findAllCourses();
    }
    return repository.findCoursesByNamePattern(coursePattern);
  }

  // 年齢が30代の生徒リストを取得
  public List<Student> getStudentsIn30s() {
    return repository.findStudentsIn30s();
  }
  // Javaコース受講者リストを取得
  public List<StudentWithCourse> getStudentsInJavaCourse(String courseName) {
    return repository.findStudentsInJavaCourse(courseName);
  }
}
