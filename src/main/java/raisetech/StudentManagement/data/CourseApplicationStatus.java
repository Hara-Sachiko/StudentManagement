package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseApplicationStatus {

  private int id;
  private int studentCourseId;
  private String status;

}
