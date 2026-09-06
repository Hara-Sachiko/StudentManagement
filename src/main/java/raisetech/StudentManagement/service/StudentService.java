package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import raisetech.StudentManagement.Exception.ResourceNotFoundException;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;


/**
 * 受講生に関するビジネスロジックを担うServiceです
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  private static final Logger log =
      LoggerFactory.getLogger(StudentService.class);

  public StudentService(
      StudentRepository repository,
      StudentConverter converter
  ) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧を検索する
   *
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> searchStudentList() {

    log.info("search student list");

    List<Student> students =
        repository.findAllStudents();

    List<StudentCourse> courses =
        repository.findAllCourses();

    List<CourseApplicationStatus> applicationStatuses =
        repository.findAllApplicationStatuses();

    return converter.convertStudentDetails(
        students,
        courses,
        applicationStatuses
    );
  }

  /**
   * 受講生詳細を取得する
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(int studentId) {

    log.info("search student id={}", studentId);

    Student student =
        repository.findStudentById(studentId);

    if (student == null) {
      throw new ResourceNotFoundException(
          "指定されたIDの受講生が存在しません。ID: " + studentId
      );
    }

    List<StudentCourse> courses =
        repository.findCoursesByStudentId(studentId);

    List<CourseApplicationStatus> applicationStatuses =
        repository.findApplicationStatusesByStudentId(studentId);

    return converter.convertStudentDetails(
        List.of(student),
        courses,
        applicationStatuses
    ).get(0);
  }

  /**
   * 受講生を新規登録する
   *
   * 受講生登録後、コースを登録し、
   * コースごとに「仮申込」の申込状況を自動作成する。
   *
   * @param student 登録する受講生
   * @param courses 登録するコース一覧
   * @return 登録後の受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentWithCourses(
      Student student,
      List<StudentCourse> courses
  ) {

    log.info("register student");

    // 受講生を登録
    repository.insertStudent(student);

    Integer studentId = student.getId();

    if (studentId == null || studentId == 0) {
      throw new IllegalStateException(
          "ID採番に失敗しています"
      );
    }

    LocalDate today = LocalDate.now();

    if (courses != null) {

      for (StudentCourse course : courses) {

        // コース名が空の場合は登録しない
        if (course.getCourseName() == null
            || course.getCourseName().isBlank()) {
          continue;
        }

        // 受講生IDを設定
        course.setStudentId(studentId);

        // コース開始日を設定
        course.setCourseStartAt(
            today.toString()
        );

        // コース終了日を6か月後に設定
        course.setCourseEndAt(
            today.plusMonths(6).toString()
        );

        // コースを登録
        repository.insertStudentCourse(course);

        // コース登録後に「仮申込」を作成
        CourseApplicationStatus applicationStatus =
            new CourseApplicationStatus();

        applicationStatus.setStudentCourseId(
            course.getId()
        );

        applicationStatus.setStatus("仮申込");

        repository.insertApplicationStatus(
            applicationStatus
        );
      }
    }

    // 登録後のコースを取得
    List<StudentCourse> registeredCourses =
        repository.findCoursesByStudentId(studentId);

    // 登録後の申込状況を取得
    List<CourseApplicationStatus> registeredApplicationStatuses =
        repository.findApplicationStatusesByStudentId(studentId);

    return converter.convertStudentDetails(
        List.of(student),
        registeredCourses,
        registeredApplicationStatuses
    ).get(0);
  }


  /**
   * 受講生情報を更新する
   *
   * @param studentDetail 更新する受講生情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    if (studentDetail == null
        || studentDetail.getStudent() == null) {

      throw new IllegalArgumentException(
          "studentDetailが不正です"
      );
    }

    log.info(
        "update student id={}",
        studentDetail.getStudent().getId()
    );

    Student student =
        repository.findStudentById(
            studentDetail.getStudent().getId()
        );

    if (student == null) {
      throw new ResourceNotFoundException(
          "更新対象の受講生が存在しません"
      );
    }

    // 受講生情報を更新
    repository.updateStudentInfo(
        studentDetail.getStudent()
    );

    List<StudentCourse> courses =
        studentDetail.getStudentCourses();

    if (courses == null) {
      return;
    }

    // コース情報を更新
    for (StudentCourse course : courses) {

      if (course.getId() == null
          || course.getId() <= 0) {

        throw new IllegalArgumentException(
            "コースIDが不正です"
        );
      }

      repository.updateStudentCourse(
          course
      );
    }
  }
}

