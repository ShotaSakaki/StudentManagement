package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {
  @Pattern(regexp = "^\\d+$", message = "数字のみを入力するようにしてください")
  private String id;

  @NotBlank
  private String lastName;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastNameFurigana;

  @NotBlank
  private String firstNameFurigana;

  private String nickname;

  @NotBlank
  @Email
  private String email;

  private String prefecture;

  @Min(value = 0, message = "年齢は0以上である必要があります")
  private int age;

  @Pattern(regexp = "^(男|女|その他)$", message = "性別は「男」「女」「その他」から選択してください")
  private String gender;
  private String remark;
  private boolean isDeleted = false;
}