package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentid}")
  List<StudentCourse> searchStudentsCourses(String studentid);


  // -----------------------
  // students テーブル
  // -----------------------
  @Select("""
      SELECT id, full_name AS fullName, furigana, nickname, region, age, gender 
      FROM students
      """)
  List<Student> findAllStudents();

  @Select("""
      SELECT id, full_name AS fullName, furigana, nickname, region, age, gender 
      FROM students 
      WHERE full_name REGEXP #{namePattern}
      """)
  List<Student> findStudentsByNamePattern(String namePattern);

  // -----------------------
  // students_courses テーブル
  // -----------------------
  @Select("""
      SELECT id, student_id AS studentId, course_name AS courseName, course_start_at AS startDate, course_end_at AS endDate
      FROM students_courses
      """)
  List<StudentCourse> findAllCourses();

  @Select("""
      SELECT id, student_id AS studentId, course_name AS courseName, course_start_at AS startDate, course_end_at AS endDate
      FROM students_courses 
      WHERE course_name REGEXP #{coursePattern}
      """)
  List<StudentCourse> findCoursesByNamePattern(String coursePattern);

  // -----------------------
  // JOIN検索
  // -----------------------
  @Select("""
          SELECT s.id AS studentId,
                 s.full_name AS studentName,
                 s.age,
                 c.course_name AS courseName
          FROM students s
          LEFT JOIN students_courses c ON s.id = c.student_id
          WHERE s.full_name REGEXP #{namePattern}
             OR c.course_name REGEXP #{coursePattern}
      """)
  List<StudentWithCourse> searchStudentsWithCourses(String namePattern, String coursePattern);

  // 年齢が30〜39歳の生徒を取得
  @Select("SELECT id, full_name AS fullName, furigana, nickname, region, age, gender " +
      "FROM students " +
      "WHERE age BETWEEN 30 AND 39")
  List<Student> findStudentsIn30s();

  // Javaコースを受講している生徒を取得（引数付き）
  @Select("""
          SELECT s.id AS studentId,
                 s.full_name AS studentName,
                 s.age,
                 c.course_name AS courseName
          FROM students s
          INNER JOIN students_courses c ON s.id = c.student_id
          WHERE c.course_name = #{courseName}
      """)
  List<StudentWithCourse> findStudentsInJavaCourse(@Param("courseName") String courseName);

  // -----------------------
  // 生徒登録処理
  // -----------------------
  @Insert("""
          INSERT INTO students (
              full_name,
              furigana,
              nickname,
              region,
              age,
              gender,
              remark,
              is_deleted
          ) VALUES (
              #{fullName},
              #{furigana},
              #{nickname},
              #{region},
              #{age},
              #{gender},
              #{remark},
              false
          )
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void insertStudent(Student student);

  // -----------------------
  // 新規追加：StudentCourse 登録（開始日付き）
  // -----------------------
  @Insert("""
            INSERT INTO students_courses (
                student_id,
                course_name,
                course_start_at,
                course_end_at
            ) VALUES (
                #{studentId},
                #{courseName},
                #{courseStartAt},
                #{courseEndAt}
            )
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  void insertStudentCourse(StudentCourse course);

  @Select("""
      SELECT 
          id,
          full_name AS fullName,
          furigana,
          nickname,
          region,
          age,
          gender,
          remark
      FROM students
      WHERE id = #{id}
      """)
  Student findStudentById(int id);


  @Select("""
      SELECT 
          id,
          student_id AS studentId,
          course_name AS courseName,
          course_start_at AS courseStartAt,
          course_end_at AS courseEndAt
      FROM students_courses
      WHERE student_id = #{studentId}
      """)
  List<StudentCourse> findCoursesByStudentId(int studentId);


  // 学生情報更新
  @Update("""
          UPDATE students SET
              full_name = #{fullName},
              furigana = #{furigana},
              nickname = #{nickname},
              region = #{region},
              age = #{age},
              gender = #{gender},
              remark = #{remark},
              is_deleted = #{isDeleted}
          WHERE id = #{id}
      """)
  void updateStudentInfo(Student student);

  // コース情報更新
  @Update("""
         UPDATE students_courses SET
           course_name = #{courseName}
         WHERE id = #{id}
      """)
  void updateStudentCourse(StudentCourse course);
}