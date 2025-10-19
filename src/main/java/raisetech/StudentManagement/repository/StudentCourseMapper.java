package raisetech.StudentManagement.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import raisetech.StudentManagement.data.StudentCourse;

@Mapper
public interface StudentCourseMapper {

  // 正規表現でフィルタ付き検索
  @Select("""
        SELECT s.name AS student_name, c.course_name
        FROM students s
        JOIN students_courses sc ON s.id = sc.student_id
        JOIN courses c ON sc.course_id = c.id
        WHERE s.name REGEXP #{regex}
           OR c.course_name REGEXP #{regex}
        """)
  List<StudentCourse> searchByRegex(@Param("regex") String regex);

  // 全件検索
  @Select("""
        SELECT s.name AS student_name, c.course_name
        FROM students s
        JOIN students_courses sc ON s.id = sc.student_id
        JOIN courses c ON sc.course_id = c.id
        """)
  List<StudentCourse> findAll();
}