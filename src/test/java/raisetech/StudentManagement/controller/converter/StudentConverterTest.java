package raisetech.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

 private StudentConverter sut;

 @BeforeEach
  void before(){
   sut  = new StudentConverter();

 }

 @Test
  void 受講生とコース情報を紐づけて受講生詳細に変換できること(){

   Student student = new Student();
   student.setId(1);
   student.setFullName("江並公史");

   StudentCourse course = new StudentCourse();
   course.setStudentId(1);
   course.setCourseName("Javaコース");

   List<StudentDetail> actual =
       sut.convertStudentDetails(List.of(student), List.of(course));

   assertEquals(1, actual.size());
   assertEquals(student, actual.get(0).getStudent());
   assertEquals(1, actual.get(0).getStudentCourses().size());
   assertEquals(course, actual.get(0).getStudentCourses().get(0));
 }

 @Test
 void コース情報が存在しない場合は空のリストになること(){

  Student student = new Student();
  student.setId(1);
  student.setFullName("江並公史");

  List<StudentDetail> actual =
      sut.convertStudentDetails(List.of(student),List.of());

  assertEquals(1, actual.size());
  assertTrue(actual.get(0).getStudentCourses().isEmpty());
 }

 @Test
 void 複数受講生を正しく紐づけられること(){

  Student student1 = new Student();
  student1.setId(1);
  student1.setFullName("山田太郎");

  Student student2 = new Student();
  student2.setId(2);
  student2.setFullName("江並公史");

  StudentCourse course1 = new StudentCourse();
  course1.setStudentId(1);
  course1.setCourseName("Java");

  StudentCourse course2 = new StudentCourse();
  course2.setStudentId(2);
  course2.setCourseName("AWS");

  List<StudentDetail> actual =
      sut.convertStudentDetails(
          List.of(student1, student2),
          List.of(course1, course2));

  assertEquals(2, actual.size());

  assertEquals(student1, actual.get(0).getStudent());
  assertEquals(student2, actual.get(1).getStudent());

  assertEquals(course1, actual.get(0).getStudentCourses().get(0));
  assertEquals(course2, actual.get(1).getStudentCourses().get(0));
 }

 @Test
 void 一人の受講生に複数コースを紐づけられること() {

  Student student = new Student();
  student.setId(1);
  student.setFullName("山田太郎");

  StudentCourse course1 = new StudentCourse();
  course1.setStudentId(1);
  course1.setCourseName("Java");

  StudentCourse course2 = new StudentCourse();
  course2.setStudentId(1);
  course2.setCourseName("AWS");

  List<StudentDetail> actual =
      sut.convertStudentDetails(
          List.of(student),
          List.of(course1, course2));

  assertEquals(1, actual.size());
  assertEquals(student, actual.get(0).getStudent());
  assertEquals(2, actual.get(0).getStudentCourses().size());

  assertEquals(course1, actual.get(0).getStudentCourses().get(0));
  assertEquals(course2, actual.get(0).getStudentCourses().get(1));
 }

 @Test
 void 受講生一覧が空の場合は空リストを返すこと() {

  List<StudentDetail> actual =
      sut.convertStudentDetails(List.of(), List.of());

  assertTrue(actual.isEmpty());
 }
}