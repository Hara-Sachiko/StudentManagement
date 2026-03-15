package raisetech.StudentManagement.controller.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  /**
   * Student + StudentCourse を StudentDetail に変換
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> students,
      List<StudentCourse> studentCourses) {

    // studentIdごとにコースをまとめる
    Map<Integer, List<StudentCourse>> courseMap =
        studentCourses.stream()
            .collect(Collectors.groupingBy(StudentCourse::getStudentId));

    return students.stream()
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