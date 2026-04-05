package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コース情報を扱うオブジェクト
 */
@Getter
@Setter

public class StudentCourse {
  private int id;
  private int studentId;
  private String courseName;
  private String courseStartAt;
  private String courseEndAt;

}