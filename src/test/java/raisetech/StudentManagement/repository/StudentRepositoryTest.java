package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void IDを指定して受講生検索ができること(){

    Student actual = sut.findStudentById(1);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getFullName()).isEqualTo("山田 太郎");
  }

  @Test
  void 指定した受講生IDのコース一覧を取得できること() {

    List<StudentCourse> actual = sut.findCoursesByStudentId(1);

    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0).getStudentId()).isEqualTo(1);
  }

  @Test
  void 受講生の全件検索が行えること() {

   List<Student> actual = sut.findAllStudents();

   assertThat(actual.size()).isEqualTo(7);

  }

  @Test
  void 名前で部分一致検索ができること(){

    List<Student> actual = sut.findStudentsByNamePattern("%山田%");

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getFullName()).contains("山田");

  }


  @Test
  void コース情報を全件取得できること(){

    List<StudentCourse> actual = sut.findAllCourses();

    assertThat(actual).isNotEmpty();
  }

  @Test
  void コース名で検索できること(){

    List<StudentCourse> actual = sut.findCoursesByNamePattern("%Java%");

    assertThat(actual).isNotEmpty();
  }

  @Test
  void Java受講生を検索できること(){

    List<StudentWithCourse> actual = sut.findStudentsInJavaCourse("Javaコース");

    assertThat(actual).isNotEmpty();
  }

  @Test
  void 受講生の登録が行えること() {

    Student student = new Student();
    student.setFullName("江並公史");
    student.setFurigana("エナミコウジ");
    student.setNickname("エナミ");
    student.setRegion("奈良");
    student.setAge(36);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.insertStudent(student);

    List<Student> actual = sut.findAllStudents();

    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void コース登録ができること() {

    StudentCourse course = new StudentCourse();
    course.setStudentId(1);
    course.setCourseName("AWS");
    course.setCourseStartAt("2026-07-26");
    course.setCourseEndAt("2027-01-26");

    sut.insertStudentCourse(course);

    List<StudentCourse> actual = sut.findCoursesByStudentId(1);

    assertThat(actual).extracting(StudentCourse::getCourseName).contains("AWS");
  }

  @Test
  void 受講生更新ができること(){

    Student student = sut.findStudentById(1);
    student.setRegion("北海道");

    sut.updateStudentInfo(student);

    Student actual = sut.findStudentById(1);

    assertThat(actual.getRegion()).isEqualTo("北海道");
  }

  @Test
  void コース更新ができること(){

    StudentCourse course = sut.findCoursesByStudentId(1).get(0);
    course.setCourseName("Spring Boot");

    sut.updateStudentCourse(course);

    StudentCourse actual = sut.findCoursesByStudentId(1).get(0);

    assertThat(actual.getCourseName()).isEqualTo("Spring Boot");
  }

  @Test
  void コース削除ができること(){

    StudentCourse course = sut.findCoursesByStudentId(1).get(0);

    sut.deleteStudentCourse(course.getId());

    List<StudentCourse> actual = sut.findCoursesByStudentId(1);

    assertThat(actual).noneMatch(c -> c.getId().equals(course.getId()));
  }

  @Test
  void 存在しない受講生IDの場合はnullが返ること() {

    Student actual = sut.findStudentById(999);

    assertThat(actual).isNull();
  }

  @Test
  void 該当する名前がない場合は空リストが返ること() {

    List<Student> actual =
        sut.findStudentsByNamePattern("%存在しない名前%");

    assertThat(actual).isEmpty();
  }
}