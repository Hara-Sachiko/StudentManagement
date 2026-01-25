package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.data.StudentCourse;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
  public List<StudentDetail> getAllStudents() {
    List<Student> students = service.getAllStudents();
    List<StudentCourse> studentCourses = service.getAllCourses();
    return converter.convertStudentDetails(students, studentCourses);
  }

  // 新規登録画面
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(new Student());
    model.addAttribute("studentDetail", detail);
    return "registerStudent";
  }

  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable int id, Model model) {
    StudentDetail detail = service.searchStudent(id);
    model.addAttribute("studentDetail", detail);
    return "updateStudent";
  }

  // 新規登録処理
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {

    if (result.hasErrors()) {
      return "registerStudent";
    }

    LocalDate today = LocalDate.now();

    for (StudentCourse course : studentDetail.getStudentCourses()) {
      course.setCourseStartAt(today.toString());
      course.setCourseEndAt(today.plusMonths(6).toString());
    }

    service.registerStudentWithCourses(
        studentDetail.getStudent(),
        studentDetail.getStudentCourses());

    return "redirect:/students";
  }



  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}