package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


/**
 * 受講生コース情報を扱うオブジェクト
 */
@Schema(description = "受講生コース情報")
@Getter
@Setter

public class StudentCourse {

  private Integer id;
  private int studentId;
  private String courseName;
  private String courseStartAt;
  private String courseEndAt;


}