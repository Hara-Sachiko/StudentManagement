package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
  private int id;
  private String fullName;
  private String furigana;
  private String nickname;
  private String region;
  private int age;
  private String gender;
}
