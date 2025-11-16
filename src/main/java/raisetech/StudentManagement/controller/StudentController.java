package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.etc.StudentWithCourse;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  // 一覧表示
  @GetMapping("/students")
  public String getAllStudents(Model model) {
    List<Student> students = service.getAllStudents();
    List<StudentCourse> studentCourses = service.getAllCourses();

    model.addAttribute("students", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  // 新規登録画面表示
  @GetMapping("/newStudent")
  public String newStudent(Model model) {

    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());   // 中身は空でOK

    model.addAttribute("studentDetail", detail);
    return "registerStudent";
  }

  // 登録処理（コース名 + 開始日を RequestParam で受け取る）
  @PostMapping("/registerStudent")
  public String registerStudent(
      @ModelAttribute StudentDetail studentDetail,
      BindingResult result,
      @RequestParam List<String> courseNames,
      @RequestParam List<String> startDates
  ) {

    if (result.hasErrors()) {
      return "registerStudent";
    }

    // 1. Student 登録
    service.registerStudent(studentDetail.getStudent());
    int studentId = studentDetail.getStudent().getId();

    // 2. コース登録（コース名 + 開始日）
    service.registerStudentCoursesWithStartDateByName(studentId, courseNames, startDates);

    return "redirect:/students";
  }
}