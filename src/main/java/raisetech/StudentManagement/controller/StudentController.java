package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです
 */
@Validated
@RestController
@RequestMapping("/api/students")
public class StudentController {

  private final StudentService service;

  private static final Logger log =
      LoggerFactory.getLogger(StudentController.class);

  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧を取得する
   *
   * @return 受講生詳細の一覧
   */
  @Operation(
      summary = "一覧検索",
      description = "受講生の一覧を検索します"
  )
  @GetMapping
  public ResponseEntity<List<StudentDetail>> getAllStudents() {

    List<StudentDetail> result = service.searchStudentList();

    return ResponseEntity.ok(result);
  }

  /**
   * 受講生詳細を取得する
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @Operation(
      summary = "詳細取得",
      description = "指定したIDの受講生を取得します"
  )
  @GetMapping("/{id}")
  public ResponseEntity<StudentDetail> getStudent(
      @PathVariable
      @Positive(message = "IDは1以上を指定してください")
      int id
  ) {

    StudentDetail result = service.searchStudent(id);

    return ResponseEntity.ok(result);
  }

  /**
   * 受講生を新規登録する
   *
   * @param studentDetail 受講生およびコース情報
   * @return 処理結果メッセージ
   */
  @Operation(
      summary = "新規登録",
      description = "受講生とコース情報を新規登録します"
  )
  @PostMapping
  public ResponseEntity<String> registerStudent(
      @RequestBody
      StudentDetail studentDetail
  ) {

    System.out.println("===== registerStudent START =====");
    System.out.println("studentDetail = " + studentDetail);

    log.info("register request: {}", studentDetail);

    service.registerStudentWithCourses(
        studentDetail.getStudent(),
        studentDetail.getStudentCourses()
    );

    System.out.println("===== registerStudent END =====");

    return ResponseEntity
        .status(201)
        .body("登録処理が成功しました");
  }

  /**
   * 受講生情報を更新する
   *
   * @param id 更新対象の受講生ID
   * @param studentDetail 更新する受講生情報
   * @return 処理結果メッセージ
   */
  @Operation(
      summary = "更新",
      description = "受講生情報を更新します"
  )
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(
      @PathVariable
      @Positive(message = "IDは1以上を指定してください")
      int id,

      @RequestBody
      @Valid
      StudentDetail studentDetail
  ) {

    log.info("update request id={}, body={}", id, studentDetail);

    studentDetail.getStudent().setId(id);

    service.updateStudent(studentDetail);

    return ResponseEntity.ok("更新処理が成功しました");
  }

  /**
   * POSTリクエストの動作確認用
   */
  @PostMapping("/test")
  public ResponseEntity<String> testPost() {

    System.out.println("===== TEST POST START =====");

    return ResponseEntity.ok("POSTテスト成功");
  }

  /**
   * GETリクエストの動作確認用
   */
  @GetMapping("/test")
  public ResponseEntity<String> testGet() {

    System.out.println("===== TEST GET START =====");

    return ResponseEntity.ok("GETテスト成功");
  }
}