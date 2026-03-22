package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧取得
   */
  public List<StudentDetail> searchStudentList() {

    List<Student> students = repository.findAllStudents();
    List<StudentCourse> courses = repository.findAllCourses();

    return converter.convertStudentDetails(students, courses);
  }

  /**
   * 受講生詳細取得
   */
  public StudentDetail searchStudent(int studentId) {

    Student student = repository.findStudentById(studentId);
    List<StudentCourse> courses = repository.findCoursesByStudentId(studentId);

    return buildStudentDetail(student, courses);
  }

  /**
   * StudentDetail生成共通処理
   */
  private StudentDetail buildStudentDetail(
      Student student,
      List<StudentCourse> courses) {

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourses(courses);

    return detail;
  }

  /**
   * 受講生登録
   */
  @Transactional
  public StudentDetail registerStudentWithCourses(
      Student student,
      List<StudentCourse> courses) {

    repository.insertStudent(student);

    int studentId = student.getId();

    LocalDate today = LocalDate.now();

    for (StudentCourse course : courses) {

      if (course.getCourseName() == null || course.getCourseName().isBlank()) {
        continue;
      }

      course.setStudentId(studentId);
      course.setCourseStartAt(today.toString());
      course.setCourseEndAt(today.plusMonths(6).toString());

      repository.insertStudentCourse(course);
    }

    List<StudentCourse> registeredCourses =
        repository.findCoursesByStudentId(studentId);

    return buildStudentDetail(student, registeredCourses);
  }

  /**
   * 受講生更新
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudentInfo(studentDetail.getStudent());

    if (studentDetail.getStudentCourses() == null) {
      return;
    }

    for (StudentCourse course : studentDetail.getStudentCourses()) {
      repository.updateStudentCourse(course);
    }
  }
}