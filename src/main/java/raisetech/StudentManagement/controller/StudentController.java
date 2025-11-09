package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.etc.StudentWithCourse;
import org.springframework.ui.Model;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/students")
  public String getAllStudents(Model model) {
    List<Student> students = service.getAllStudents();
    List<StudentCourse> studentCourses = service.getAllCourses();

    model.addAttribute("students", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }


  @GetMapping("/students/search")
  public List<Student> searchStudents(@RequestParam(required = false) String namePattern) {
    return service.searchStudents(namePattern);
  }

  @GetMapping("/courses")
  public List<StudentCourse> getAllCourses() {
    return service.getAllCourses();
  }

  @GetMapping("/courses/search")
  public List<StudentCourse> searchCourses(@RequestParam(required = false) String coursePattern) {
    return service.searchCourses(coursePattern);
  }

  @GetMapping("/students/30s")
  public List<Student> getStudentsIn30s() {
    return service.getStudentsIn30s();
  }

  @GetMapping("/students/java")
  public List<StudentWithCourse> getStudentsInJavaCourse() {
    return service.getStudentsInJavaCourse("Javaコース"); // ←固定値
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model){
    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

    // ★ 登録処理呼び出し
    studentService.registerStudent(studentDetail.getStudent());

    // 登録完了後、一覧画面へリダイレクト
    return "redirect:/students";
  }
}