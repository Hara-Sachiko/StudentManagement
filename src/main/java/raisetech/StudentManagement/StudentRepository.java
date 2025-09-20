package raisetech.StudentManagement;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StudentRepository {

  // -----------------------
  // students テーブル
  // -----------------------

  // 全件取得
  @Select("SELECT * FROM students")
  List<Student> findAllStudents();

  // 名前で正規表現検索
  @Select("SELECT * FROM students WHERE name REGEXP #{namePattern}")
  List<Student> findStudentsByNamePattern(String namePattern);

  // -----------------------
  // students_courses テーブル
  // -----------------------

  // 全件取得
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> findAllCourses();

  // コース名で正規表現検索
  @Select("SELECT * FROM students_courses WHERE course_name REGEXP #{coursePattern}")
  List<StudentCourse> findCoursesByNamePattern(String coursePattern);

  // -----------------------
  // JOIN検索
  // -----------------------

  // 学生名 OR コース名で正規表現検索
  @Select("""
        SELECT s.id AS studentId,
               s.name AS studentName,
               s.age,
               c.course_name AS courseName
        FROM students s
        LEFT JOIN students_courses c ON s.id = c.student_id
        WHERE s.name REGEXP #{namePattern}
           OR c.course_name REGEXP #{coursePattern}
    """)
  List<StudentWithCourse> searchStudentsWithCourses(String namePattern, String coursePattern);

  // コース名に "J" が含まれる学生を取得
  @Select("""
        SELECT s.id AS studentId,
               s.name AS studentName,
               s.age,
               c.course_name AS courseName
        FROM students s
        INNER JOIN students_courses c ON s.id = c.student_id
        WHERE c.course_name REGEXP 'J'
    """)
  List<StudentWithCourse> findStudentsByCourseJ();
}