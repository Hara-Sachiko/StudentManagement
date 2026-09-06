package raisetech.StudentManagement.controller.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import raisetech.StudentManagement.data.CourseApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生情報、受講生コース、コース申込状況を結合し
 * 受講生詳細情報（StudentDetail）へ変換するコンバーター
 */
@Component
public class StudentConverter {

  /**
   * 受講生一覧、受講生コース一覧、コース申込状況一覧を紐付けて
   * 受講生詳細一覧に変換する
   *
   * @param studentList 受講生一覧
   * @param studentCourseList 受講生コース一覧
   * @param applicationStatusList コース申込状況一覧
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentList,
      List<StudentCourse> studentCourseList,
      List<CourseApplicationStatus> applicationStatusList) {

    // studentIdごとにコースをまとめる
    Map<Integer, List<StudentCourse>> courseMap =
        studentCourseList.stream()
            .collect(Collectors.groupingBy(StudentCourse::getStudentId));

    // studentCourseIdごとに申込状況をまとめる
    Map<Integer, List<CourseApplicationStatus>> applicationStatusMap =
        applicationStatusList.stream()
            .collect(Collectors.groupingBy(
                CourseApplicationStatus::getStudentCourseId
            ));

    return studentList.stream()
        .map(student -> {

          StudentDetail detail = new StudentDetail();

          detail.setStudent(student);

          List<StudentCourse> courses =
              courseMap.getOrDefault(
                  student.getId(),
                  List.of()
              );

          detail.setStudentCourses(courses);

          // この受講生のコースに紐づく申込状況を取得
          List<CourseApplicationStatus> applicationStatuses =
              courses.stream()
                  .flatMap(course ->
                      applicationStatusMap
                          .getOrDefault(course.getId(), List.of())
                          .stream()
                  )
                  .toList();

          detail.setApplicationStatuses(applicationStatuses);

          return detail;

        })
        .toList();
  }
}