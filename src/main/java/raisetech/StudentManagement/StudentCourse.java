package raisetech.StudentManagement;

public class StudentCourse {
  private Integer id;
  private Integer studentId;
  private String courseName;

  // getter / setter
  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Integer getStudentId() { return studentId; }
  public void setStudentId(Integer studentId) { this.studentId = studentId; }
  public String getCourseName() { return courseName; }
  public void setCourseName(String courseName) { this.courseName = courseName; }
}