package raisetech.StudentManagement.controller.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 *受講生情報と受講生コースを結合し
 *受講生詳細情報（StudentDetail）へ変換するコンバーター
 */
@Component
public class StudentConverter {

  /**
   * 受講生一覧と受講生コース一覧を紐付けて
   * 受講生詳細一覧に変換する
   *
   * @param studentList  受講生一覧
   * @param studentCourseList  受講生コース一覧
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentList,
      List<StudentCourse> studentCourseList) {

    // studentIdごとにコースをまとめる
    Map<Integer, List<StudentCourse>> courseMap =
        studentCourseList.stream()
            .collect(Collectors.groupingBy(StudentCourse::getStudentId));

    return studentList.stream()
        .map(student -> {

          StudentDetail detail = new StudentDetail();
          detail.setStudent(student);

          List<StudentCourse> courses =
              courseMap.getOrDefault(student.getId(), List.of());

          detail.setStudentCourses(courses);

          return detail;

        })
        .toList();
  }
}