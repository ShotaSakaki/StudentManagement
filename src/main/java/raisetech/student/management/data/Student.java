package raisetech.student.management.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
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
  private String email;

  private String prefecture;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}