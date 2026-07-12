package raisetech.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import raisetech.StudentManagement.Exception.ResourceNotFoundException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    when(repository.findAllStudents()).thenReturn(studentList);
    when(repository.findAllCourses()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList))
        .thenReturn(expected);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).findAllStudents();
    verify(repository, times(1)).findAllCourses();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細を取得する_リポジトリの処理が適切に呼び出せていること() {

    int studentId = 1;

    Student student = new Student();
    student.setId(studentId);
    student.setFullName("山田");

    StudentCourse course = new StudentCourse();
    course.setStudentId(studentId);
    course.setCourseName("Java");

    List<StudentCourse> courses = List.of(course);

    when(repository.findStudentById(studentId)).thenReturn(student);
    when(repository.findCoursesByStudentId(studentId)).thenReturn(courses);

    StudentDetail actual = sut.searchStudent(studentId);

    verify(repository, times(1)).findStudentById(studentId);
    verify(repository, times(1)).findCoursesByStudentId(studentId);

    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(courses, actual.getStudentCourses());

  }

  @Test
  void 受講生詳細を取得する_受講生が存在しない場合例外が発生すること() {

    int studentId = 999;

    when(repository.findStudentById(studentId)).thenReturn(null);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> sut.searchStudent(studentId));

    verify(repository, times(1)).findStudentById(studentId);
    verify(repository, times(0)).findCoursesByStudentId(studentId);

    Assertions.assertEquals("指定されたIDの受講生が存在しません。ID: 999",
        exception.getMessage());
  }

  @Test
  void 受講生登録_受講生とコースが正常に登録されていること() {

    Student student = new Student();
    student.setId(1);
    student.setFullName("山田");

    StudentCourse course = new StudentCourse();
    course.setCourseName("Java");

    List<StudentCourse> courses = List.of(course);

    when(repository.findCoursesByStudentId(1)).thenReturn(courses);

    StudentDetail actual = sut.registerStudentWithCourses(student, courses);

    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(1)).insertStudentCourse(course);
    verify(repository, times(1)).findCoursesByStudentId(1);

    Assertions.assertEquals(student, actual.getStudent());
    Assertions.assertEquals(courses, actual.getStudentCourses());
    Assertions.assertEquals(1, course.getStudentId());
  }

  @Test
  void 受講生登録_ID採番失敗時は例外が発生すること() {

    Student student = new Student();
    student.setId(0);

    List<StudentCourse> courses = List.of();

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> sut.registerStudentWithCourses(
                student,
                courses
            )
        );

    verify(repository, times(1))
        .insertStudent(student);

    verify(repository, times(0))
        .insertStudentCourse(any());

    Assertions.assertEquals(
        "ID採番に失敗しています",
        exception.getMessage()
    );
  }

  @Test
  void 受講生更新_受講生情報とコース情報が更新できること() {

    Student student = new Student();
    student.setId(1);
    student.setFullName("更新後");

    StudentCourse course = new StudentCourse();

    course.setId(1);
    course.setCourseName("Java");

    StudentDetail detail = new StudentDetail();

    detail.setStudent(student);
    detail.setStudentCourses(List.of(course));

    when(repository.findStudentById(1)).thenReturn(student);
    sut.updateStudent(detail);

    verify(repository, times(1)).findStudentById(1);
    verify(repository, times(1)).updateStudentInfo(student);
    verify(repository, times(1)).updateStudentCourse(course);
  }

  @Test
  void 受講生更新_受講生が存在しない場合例外が発生すること() {

    Student student = new Student();
    student.setId(999);

    StudentDetail detail = new StudentDetail();

    detail.setStudent(student);

    when(repository.findStudentById(999))
        .thenReturn(null);

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> sut.updateStudent(detail)
        );

    verify(repository, times(1))
        .findStudentById(999);

    verify(repository, times(0))
        .updateStudentInfo(any());

    Assertions.assertEquals(
        "更新対象の受講生が存在しません",
        exception.getMessage()
    );
  }

  @Test
  void 受講生更新_コースIDが不正な場合例外が発生すること() {

    Student student = new Student();
    student.setId(1);

    StudentCourse course = new StudentCourse();

    course.setId(null);

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourses(List.of(course));

    when(repository.findStudentById(1))
        .thenReturn(student);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.updateStudent(detail)
        );

    verify(repository, times(1))
        .findStudentById(1);

    verify(repository, times(1))
        .updateStudentInfo(student);

    verify(repository, times(0))
        .updateStudentCourse(any());

    Assertions.assertEquals(
        "コースIDが不正です",
        exception.getMessage()
    );
  }

  @Test
  void 受講生更新_studentDetailがnullの場合例外が発生すること() {

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> sut.updateStudent(null)
        );

    verify(repository, times(0))
        .findStudentById(anyInt());

    verify(repository, times(0))
        .updateStudentInfo(any());

    Assertions.assertEquals(
        "studentDetailが不正です",
        exception.getMessage()
    );
  }
}