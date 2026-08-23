package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.*;
import java.util.List;

import raisetech.StudentManagement.data.CourseApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;

/**
 * 受講生テーブル、受講生コース情報テーブル、
 * コース申込状況テーブルを扱うRepositoryです
 */
@Mapper
public interface StudentRepository {


  // ============================================================
  // 単体検索
  // ============================================================

  /**
   * 指定したIDの受講生を取得する。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student findStudentById(int id);

  /**
   * 指定した受講生IDに紐づくコース一覧を取得する。
   *
   * @param studentId 受講生ID
   * @return コース一覧
   */
  List<StudentCourse> findCoursesByStudentId(int studentId);

  /**
   * 指定した受講生コースIDに紐づく申込状況を取得する。
   *
   * @param studentCourseId 受講生コースID
   * @return 申込状況
   */
  CourseApplicationStatus findApplicationStatusByStudentCourseId(
      int studentCourseId
  );

  /**
   * 指定した受講生IDに紐づく申込状況を取得する。
   *
   * @param studentId 受講生ID
   * @return 申込状況一覧
   */
  List<CourseApplicationStatus> findApplicationStatusesByStudentId(
      @Param("studentId") int studentId
  );

  // ============================================================
  // students テーブル
  // ============================================================

  /**
   * 論理削除されていない受講生を全件取得する。
   *
   * @return 受講生一覧
   */
  List<Student> findAllStudents();

  /**
   * 名前の部分一致で受講生を検索する。
   *
   * @param namePattern 検索キーワード
   * @return 該当する受講生一覧
   */
  List<Student> findStudentsByNamePattern(
      String namePattern
  );

  /**
   * 年齢が30代の受講生を取得する。
   *
   * @return 30代の受講生一覧
   */
  List<Student> findStudentsIn30s();

  // ============================================================
  // students_courses テーブル
  // ============================================================

  /**
   * 受講生のコース情報を全件検索する。
   *
   * @return 受講生のコース情報一覧
   */
  List<StudentCourse> findAllCourses();

  /**
   * コース名の部分一致で検索する。
   *
   * @param coursePattern コース名の検索キーワード
   * @return 該当するコース一覧
   */
  List<StudentCourse> findCoursesByNamePattern(
      String coursePattern
  );

  // ============================================================
  // JOIN 検索
  // ============================================================

  /**
   * 指定したコースを受講している受講生を検索する。
   *
   * @param courseName コース名
   * @return コースを受講している受講生一覧
   */
  List<StudentWithCourse> findStudentsInJavaCourse(
      @Param("courseName") String courseName
  );
  // ============================================================
  // CourseApplicationStatus
  // ============================================================

  /**
   * すべてのコース申込状況を取得する。
   *
   * @return コース申込状況一覧
   */
  List<CourseApplicationStatus> findAllApplicationStatuses();

  // ============================================================
  // 新規登録
  // ============================================================

  /**
   * 受講生を新規登録する。
   * IDはデータベースで自動採番する。
   *
   * @param student 受講生
   */
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録する。
   * 主キーIDはデータベースの自動採番により生成され、
   * 登録後に引数のオブジェクトへ設定される。
   *
   * @param course 登録対象の受講生コース情報
   */
  void insertStudentCourse(StudentCourse course);

  /**
   * 受講生コースの申込状況を新規登録する。
   *
   * @param status 登録対象の申込状況
   */
  void insertApplicationStatus(
      CourseApplicationStatus status
  );
  // ============================================================
  // 更新処理
  // ============================================================

  /**
   * 受講生を更新する。
   *
   * @param student 受講生
   */
  void updateStudentInfo(Student student);

  /**
   * 受講生コース情報を更新する。
   *
   * @param course 受講生コース情報
   */
  void updateStudentCourse(StudentCourse course);

  // ============================================================
  // 削除処理
  // ============================================================

  /**
   * 受講生コース情報を物理削除する。
   *
   * @param id コースID
   */
  void deleteStudentCourse(int id);
}