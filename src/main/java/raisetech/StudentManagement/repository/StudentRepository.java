package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.*;
import java.util.List;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです
 */
@Mapper
public interface StudentRepository {

  /**
   * 指定したIDの受講生を取得する
   *
   * @param id 受講生ID
   * @return 受講生
   */
  // ============================================================
  // 単体検索
  // ============================================================

  Student findStudentById(int id);
  List<StudentCourse> findCoursesByStudentId(int studentId);



  /**
   * 指定した受講生IDに紐づくコース一覧を取得する
   *
   * @param studentId 受講生ID
   *  @return コース一覧
   */
  // ============================================================
  // students テーブル
  // ============================================================
  /**
   * 論理削除されていない受講生を全件取得する
   *
   * @return 受講生一覧
   */
  List<Student> findAllStudents();

  /**
   *  名前の部分一致で受講生を検索する
   *
   * @param namePattern 検索キーワード
   *  @return 該当する受講生一覧
   */
  List<Student> findStudentsByNamePattern(String namePattern);

  /**
   *  年齢が30代の受講生を取得する
   *
   *  @return 30代の受講生一覧
   */
  // 30代の生徒
  List<Student> findStudentsIn30s();


  // ============================================================
  // students_courses テーブル
  // ============================================================
  /**
   * 受講生のコース情報の全件検索を行います
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> findAllCourses();

  /**
   *  コース名の部分一致で検索する
   *
   * @param coursePattern コース名の検索キーワード
   * @return 該当するコース一覧
   */
  List<StudentCourse> findCoursesByNamePattern(String coursePattern);


  // ============================================================
  // JOIN 検索
  // ============================================================
  /**
   * 指定したコースを受講している受講生を検索します
   *
   * @param courseName コース名
   * @return コースを受講している受講生一覧
   */
  List<StudentWithCourse> findStudentsInJavaCourse(@Param("courseName") String courseName);


  // ============================================================
  // 新規登録
  // ============================================================
  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う
   *
   * @param student 受講生
   */
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。
   * 主キーIDはデータベースの自動採番により生成され、
   * 登録後に引数のオブジェクトへ設定されます。
   *
   * @param course 登録対象の受講生コース情報（IDは未設定であること）
   */
    void insertStudentCourse(StudentCourse course);


  // ============================================================
  // 更新処理
  // ============================================================
  /**
   * 受講生を更新します
   *
   * @param student 受講生
   */
  void updateStudentInfo(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
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

