package raisetech.StudentManagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

  private final StudentService service;

  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧
   */
  @GetMapping
  public List<StudentDetail> getAllStudents() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細
   */
  @GetMapping("/{id}")
  public StudentDetail getStudent(@PathVariable int id) {
    return service.searchStudent(id);
  }

  /**
   * 新規登録
   */
  @PostMapping
  public ResponseEntity<String> registerStudent(
      @RequestBody StudentDetail studentDetail
  ) {

    service.registerStudentWithCourses(
        studentDetail.getStudent(),
        studentDetail.getStudentCourses()
    );

    return ResponseEntity.ok("登録処理が成功しました");
  }

  /**
   * 更新
   */
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(
      @PathVariable int id,
      @RequestBody StudentDetail studentDetail
  ) {

    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);

    return ResponseEntity.ok("更新処理が成功しました");
  }
}