package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.*;
import java.util.List;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;

@Mapper
public interface StudentRepository {

  // ============================================================
  // 単体検索
  // ============================================================

  @Select("""
            SELECT 
                id,
                full_name AS fullName,
                furigana,
                nickname,
                region,
                age,
                gender,
                remark,
                is_deleted AS isDeleted
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



  // ============================================================
  // students テーブル
  // ============================================================

  @Select("""
            SELECT 
                id,
                full_name AS fullName,
                furigana,
                nickname,
                region,
                age,
                gender,
                remark,
                is_deleted AS isDeleted
            FROM students
            WHERE is_deleted = false
            """)
  List<Student> findAllStudents();


  @Select("""
            SELECT 
                id,
                full_name AS fullName,
                furigana,
                nickname,
                region,
                age,
                gender,
                remark,
                is_deleted AS isDeleted
            FROM students
            WHERE full_name REGEXP #{namePattern}
            AND is_deleted = false
            """)
  List<Student> findStudentsByNamePattern(String namePattern);


  // 30代の生徒
  @Select("""
            SELECT 
                id,
                full_name AS fullName,
                furigana,
                nickname,
                region,
                age,
                gender,
                remark,
                is_deleted AS isDeleted
            FROM students
            WHERE age BETWEEN 30 AND 39
            AND is_deleted = false
            """)
  List<Student> findStudentsIn30s();



  // ============================================================
  // students_courses テーブル
  // ============================================================

  @Select("""
            SELECT
                id,
                student_id AS studentId,
                course_name AS courseName,
                course_start_at AS courseStartAt,
                course_end_at AS courseEndAt
            FROM students_courses
            """)
  List<StudentCourse> findAllCourses();


  @Select("""
            SELECT 
                id,
                student_id AS studentId,
                course_name AS courseName,
                course_start_at AS courseStartAt,
                course_end_at AS courseEndAt
            FROM students_courses
            WHERE course_name REGEXP #{coursePattern}
            """)
  List<StudentCourse> findCoursesByNamePattern(String coursePattern);



  // ============================================================
  // JOIN 検索
  // ============================================================

  @Select("""
            SELECT 
                s.id AS studentId,
                s.full_name AS studentName,
                s.age,
                c.course_name AS courseName
            FROM students s
            INNER JOIN students_courses c ON s.id = c.student_id
            WHERE c.course_name = #{courseName}
            """)
  List<StudentWithCourse> findStudentsInJavaCourse(@Param("courseName") String courseName);



  // ============================================================
  // 新規登録
  // ============================================================

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



  // ============================================================
  // 更新処理
  // ============================================================

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



  @Update("""
            UPDATE students_courses SET
                course_name = #{courseName},
                course_start_at = #{courseStartAt},
                course_end_at = #{courseEndAt}
            WHERE id = #{id}
            """)
  void updateStudentCourse(StudentCourse course);



  // ============================================================
  // 削除処理（必要なら）
  // ============================================================

  @Delete("DELETE FROM students_courses WHERE id = #{id}")
  void deleteStudentCourse(int id);
}