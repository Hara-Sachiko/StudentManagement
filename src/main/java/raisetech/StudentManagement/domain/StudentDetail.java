package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生の詳細情報
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  private Student student = new Student();

  @Valid
  private List<StudentCourse> studentCourses = new ArrayList<>();

}