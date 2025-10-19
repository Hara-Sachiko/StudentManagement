package raisetech.StudentManagement.etc;

public class StudentWithCourse {
  private Integer studentId;
  private String studentName;
  private Integer age;
  private String courseName;

  // getter / setter
  public Integer getStudentId() { return studentId; }
  public void setStudentId(Integer studentId) { this.studentId = studentId; }

  public String getStudentName() { return studentName; }
  public void setStudentName(String studentName) { this.studentName = studentName; }

  public Integer getAge() { return age; }
  public void setAge(Integer age) { this.age = age; }

  public String getCourseName() { return courseName; }
  public void setCourseName(String courseName) { this.courseName = courseName; }
}
