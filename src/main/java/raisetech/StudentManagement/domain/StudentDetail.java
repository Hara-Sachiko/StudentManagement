package raisetech.StudentManagement.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;                // 生徒情報
  private List<StudentCourse> studentCourses; // 登録済み・既存コース情報
  private List<Integer> courseIds;        // フォームで選択されたコースID
  private List<String> courseStartDates;  // 選択されたコースの開始日
  private String newCourseName;           // 新規追加コース用
}
