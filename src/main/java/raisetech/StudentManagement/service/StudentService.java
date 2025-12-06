package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.etc.StudentWithCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import java.util.ArrayList;

@Service
public class StudentService {

  private final StudentRepository repository;

  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  // -----------------------
  // 学生一覧取得
  // -----------------------
  public List<Student> getAllStudents() {
    return repository.findAllStudents();
  }

  // -----------------------
  // 学生検索（詳細＋コース）
  // -----------------------
  public StudentDetail searchStudent(int studentId){
    Student student = repository.findStudentById(studentId);

    // null が返る可能性があるので対策
    List<StudentCourse> studentsCourses =
        repository.findCoursesByStudentId(studentId);

    if (studentsCourses == null) {
      studentsCourses = new ArrayList<>();
    }

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentsCourses);

    return studentDetail;
  }

  // -----------------------
  // コース一覧取得
  // -----------------------
  public List<StudentCourse> getAllCourses() {
    return repository.findAllCourses();
  }

  // -----------------------
  // 名前検索
  // -----------------------
  public List<Student> searchStudents(String namePattern) {
    if (namePattern == null || namePattern.isEmpty()) {
      return repository.findAllStudents();
    }
    return repository.findStudentsByNamePattern(namePattern);
  }

  // -----------------------
  // コース検索
  // -----------------------
  public List<StudentCourse> searchCourses(String coursePattern) {
    if (coursePattern == null || coursePattern.isEmpty()) {
      return repository.findAllCourses();
    }
    return repository.findCoursesByNamePattern(coursePattern);
  }

  // -----------------------
  // 年齢が30代の生徒リスト
  // -----------------------
  public List<Student> getStudentsIn30s() {
    return repository.findStudentsIn30s();
  }

  // -----------------------
  // Javaコース受講者リスト
  // -----------------------
  public List<StudentWithCourse> getStudentsInJavaCourse(String courseName) {
    return repository.findStudentsInJavaCourse(courseName);
  }

  // -----------------------
  // 学生登録 + コース登録（開始日と終了日自動セット）
  // -----------------------
  @Transactional
  public Student registerStudentWithCourses(Student student, List<StudentCourse> courses) {

    // 1. 学生を登録（IDセットされる）
    repository.insertStudent(student);
    int generatedStudentId = student.getId();

    LocalDate today = LocalDate.now();

    // 2. コース登録
    for (StudentCourse course : courses) {
      course.setStudentId(generatedStudentId);
      course.setCourseStartAt(today.toString());
      course.setCourseEndAt(today.plusMonths(6).toString());
      repository.insertStudentCourse(course);
    }

    return student;
  }

  // -----------------------
  // 受講生詳細取得
  // -----------------------
  public StudentDetail findStudentDetail(int studentId) {
    Student student = repository.findStudentById(studentId);
    List<StudentCourse> courses = repository.findCoursesByStudentId(studentId);

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourses(courses);
    return detail;
  }

  // -----------------------
  // 学生＋コース更新
  // -----------------------
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // 学生情報更新
    repository.updateStudentInfo(studentDetail.getStudent());

    // コース情報更新
    for (StudentCourse course : studentDetail.getStudentCourses()) {
      repository.updateStudentCourse(course);
    }
  }
}