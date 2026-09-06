package raisetech.StudentManagement.Exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log =
      LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // 404
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleNotFound(
      ResourceNotFoundException ex) {

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
  }

  // 400（独自例外）
  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(
      TestException ex) {

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }

  // 400（@RequestBody @Valid）
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(
      MethodArgumentNotValidException ex) {

    log.error("入力値バリデーションエラー", ex);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("入力値が不正です");
  }

  // 400（@PathVariable や @RequestParam のバリデーション）
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(
      ConstraintViolationException ex) {

    log.error("入力値バリデーションエラー", ex);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }

  // 400（JSON読み込みエラー）
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {

    log.error("JSON読み込みエラー", ex);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("JSONの形式が不正です: " + ex.getMessage());
  }

  // 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {

    log.error("予期しないエラー", ex);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("サーバーエラーが発生しました");
  }
}