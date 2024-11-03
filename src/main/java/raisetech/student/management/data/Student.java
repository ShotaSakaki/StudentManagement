package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
  private String id;
  private String lastName;
  private String firstName;
  private String lastNameFurigana;
  private String firstNameFurigana;
  private String nickname;
  private String email;
  private String prefecture;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}