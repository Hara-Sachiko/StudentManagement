package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;

/**
 * 受講生を扱うオブジェクト
 */
@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private Integer id;

  @NotBlank
  private String fullName;

  @NotBlank
  private String furigana;

  @NotBlank
  private String nickname;

  @NotBlank
  private String region;

  private int age;

  @NotBlank
  private String gender;

  private String remark;
  private boolean isDeleted;
}
