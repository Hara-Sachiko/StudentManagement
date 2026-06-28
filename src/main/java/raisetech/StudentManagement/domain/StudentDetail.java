package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "受講生詳細情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  @NotNull(message = "受講生情報は必須です")
  @Schema(description = "受講生情報")
  private Student student = new Student();

  @Valid
  @Schema(description = "受講生コース一覧")
  private List<StudentCourse> studentCourses = new ArrayList<>();
}