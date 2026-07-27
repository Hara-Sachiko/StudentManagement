package raisetech.StudentManagement.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
@ImportAutoConfiguration(exclude = MybatisAutoConfiguration.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/api/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生一覧検索が実行できていること() throws Exception{
    StudentDetail studentDetail = new StudentDetail();
    when(service.searchStudent(1)).thenReturn(studentDetail);

    mockMvc.perform(get("/api/students/1"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(1);
  }

  @Test
  void IDが0の場合は入力チェックに掛かること() throws Exception {

    mockMvc.perform(get("/api/students/0"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).searchStudent(0);
  }

  @Test
  void 受講生を登録ができること() throws Exception{
    String json = """
        {
          "student":{
            "id":1,
            "fullName":"江並公史",
            "furigana":"エナミコウジ",
            "nickname":"コウジ",
            "region":"奈良県",
            "age":20,
            "gender":"男性"
          },
          "studentCourses":[]
        }
        """;

    mockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());

    verify(service, times(1))
        .registerStudentWithCourses(any(), any());
  }

  @Test
  void 氏名が漢字以外の場合は入力チェックに掛かること() throws Exception {

    String json = """
        {
          "student":{
            "id":1,
            "fullName":"てすと",
            "furigana":"エナミコウジ",
            "nickname":"コウジ",
            "region":"奈良県",
            "age":20,
            "gender":"男性"
          },
          "studentCourses":[]
        }
        """;

    mockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());

    verify(service, times(0))
        .registerStudentWithCourses(any(), any());
  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと(){
    Student student = new Student();
    student.setId(1);
    student.setFullName("江並公史");
    student.setFurigana("エナミコウジ");
    student.setNickname("コウジ");
    student.setRegion("奈良県");
    student.setGender("男性");
    student.setAge(20);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);

  }

  @Test
  void 氏名に漢字以外を入力した時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId(1);
    student.setFullName("テストです");
    student.setFurigana("エナミコウジ");
    student.setNickname("コウジ");
    student.setRegion("奈良県");
    student.setGender("男性");
    student.setAge(20);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations)
        .extracting("message")
        .containsOnly("氏名は漢字で入力してください");
  }

  @Test
  void 受講生コースで適切な値を入力した時に入力チェックに異常が発生しないこと(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt("2026-07-19");
    studentCourse.setCourseEndAt("2027-01-19");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations).isEmpty();

  }

  @Test
  void コース名が空の場合は入力チェックに掛かること(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("");
    studentCourse.setCourseStartAt("2026-07-19");
    studentCourse.setCourseEndAt("2027-01-19");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations).hasSize(1);
    assertThat(violations)
        .extracting("message")
        .containsOnly("コース名は必須です");
  }

}