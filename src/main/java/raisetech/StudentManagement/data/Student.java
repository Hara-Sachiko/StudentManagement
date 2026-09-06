package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生を扱うオブジェクト
 */
@Schema(description = "受講生情報")
@Getter
@Setter
public class Student {

  @Positive
  @Schema(description = "受講生ID")
  private Integer id;

  @NotBlank(message = "氏名は必須です")
  @Pattern(regexp = "^[一-龥々]+$", message = "氏名は漢字で入力してください")
  @Schema(description = "氏名")
  private String fullName;

  @NotBlank(message = "フリガナは必須です")
  @Schema(description = "フリガナ")
  private String furigana;

  @NotBlank(message = "ニックネームは必須です")
  @Schema(description = "ニックネーム")
  private String nickname;

  @NotBlank(message = "地域は必須です")
  @Schema(description = "地域")
  private String region;

  @NotNull
  @Min(value = 0, message = "年齢は0以上を指定してください")
  @Max(value = 150, message = "年齢は150以下を指定してください")
  @Schema(description = "年齢", example = "25")
  private Integer age;

  @Schema(description = "性別", example = "男性")
  private String gender;

  @Schema(description = "備考", example = "Javaコース受講中")
  private String remark;

  @Schema(description = "論理削除フラグ", example = "false")
  private boolean isDeleted;
}
