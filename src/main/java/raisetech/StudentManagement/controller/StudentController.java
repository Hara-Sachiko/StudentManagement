package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.Exception.TestException;
import java.util.ArrayList;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです
 */
@Validated
@RestController
@RequestMapping("/api/students")
public class StudentController {

  private final StudentService service;

  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧を取得する
   *
   * @return 受講生詳細の一覧
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します")
  @GetMapping
  public List<StudentDetail> getAllStudents(){
    return service.searchStudentList();
  }

  /**
   * 指定したIDの受講生詳細を取得する
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/{id}")
  public StudentDetail getStudent(@PathVariable @Positive int id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生を新規登録する
   *
   * @param studentDetail 受講生およびコース情報
   * @return 処理結果メッセージ
   */
  @PostMapping
  public ResponseEntity<String> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail
  ) {
    System.out.println(studentDetail);

    if (studentDetail.getStudent() == null) {
      return ResponseEntity.badRequest().body("studentがnullです");
    }

    service.registerStudentWithCourses(
        studentDetail.getStudent(),
        studentDetail.getStudentCourses()
    );

    return ResponseEntity.ok("登録処理が成功しました");
  }

  /**
   * 受講生情報を更新する
   *
   * @param id            更新対象の受講生ID
   * @param studentDetail 更新する受講生情報
   * @return 処理結果メッセージ
   */
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(
      @PathVariable int id,
      @RequestBody @Valid StudentDetail studentDetail
  ) {

    if (studentDetail == null || studentDetail.getStudent() == null) {
      return ResponseEntity.badRequest().body("studentがnullです");
    }

    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);

    return ResponseEntity.ok("更新処理が成功しました");
  }
}