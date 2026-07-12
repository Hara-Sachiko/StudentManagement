package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コース情報を扱うオブジェクト
 */
@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "コースID")
  private Integer id;

  @Positive(message = "受講生IDは1以上を指定してください")
  @Schema(description = "受講生ID")
  private int studentId;

  @NotBlank(message = "コース名は必須です")
  @Schema(description = "コース名")
  private String courseName;

  @NotBlank(message = "受講開始日を入力してください")
  @Schema(description = "受講開始日")
  private String courseStartAt;

  @NotBlank(message = "受講終了日を入力してください")
  @Schema(description = "受講終了日")
  private String courseEndAt;
}