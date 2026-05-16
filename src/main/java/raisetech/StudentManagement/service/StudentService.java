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
import raisetech.StudentManagement.Exception.ResourceNotFoundException;

/**
 * 受講生に関するビジネスロジックを担うサービスクラス。
 *  Repositoryから取得したデータを加工し、Controllerへ提供する
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受受講生一覧を取得する。
   *  受講生情報とコース情報を結合して返却する
   *
   *   @return 受講生詳細一覧
   */
  public List<StudentDetail> searchStudentList() {

    List<Student> students = repository.findAllStudents();
    List<StudentCourse> courses = repository.findAllCourses();

    return converter.convertStudentDetails(students, courses);
  }

  /**
   * 指定した受講生の詳細情報を取得する
   *
   * @param studentId 受講生ID
   *  @return 受講生詳細
   */
  public StudentDetail searchStudent(int studentId) {

    Student student = repository.findStudentById(studentId);

    if (student == null) {
      throw new ResourceNotFoundException(
          "指定されたIDの受講生が存在しません。ID: " + studentId);
    }

    List<StudentCourse> courses = repository.findCoursesByStudentId(studentId);

    return buildStudentDetail(student, courses);
  }

  /**
   * StudentDetail生成共通処理
   *
   * @param student 受講生情報
   * @param courses コース情報一覧
   * @return 受講生詳細
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
   * 受講生およびコース情報を登録する。
   * トランザクション管理により、途中で失敗した場合は全てロールバックされる。
   *
   *  @param student 登録する受講生
   *  @param courses 登録するコース一覧
   *  @return 登録後の受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentWithCourses(
      Student student,
      List<StudentCourse> courses) {

    repository.insertStudent(student);

    Integer studentId = student.getId();

    if (studentId == null || studentId == 0) {
      throw new IllegalStateException("受講生IDの取得に失敗しました");
    }

    LocalDate today = LocalDate.now();

    if (courses != null) {
      for (StudentCourse course : courses) {

        if (course.getCourseName() == null || course.getCourseName().isBlank()) {
          continue;
        }

        course.setStudentId(studentId);
        course.setCourseStartAt(today.toString());
        course.setCourseEndAt(today.plusMonths(6).toString());

        repository.insertStudentCourse(course);
      }
    }

      List<StudentCourse> registeredCourses =
          repository.findCoursesByStudentId(studentId);

      return buildStudentDetail(student, registeredCourses);
    }


  /**
   * 受講生情報およびコース情報を更新する
   *
   *  @param studentDetail 更新対象の受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudentInfo(studentDetail.getStudent());

    if (studentDetail.getStudentCourses() == null) {
      return;
    }

    for (StudentCourse course : studentDetail.getStudentCourses()) {

      if (course.getId() == 0) {
        throw new IllegalArgumentException("コースIDが未設定です");
      }

      repository.updateStudentCourse(course);
    }
  }
}