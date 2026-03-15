package raisetech.StudentManagement.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentViewController {

  private final StudentService service;

  public StudentViewController(StudentService service) {
    this.service = service;
  }

  /**
   * 新規登録画面
   */
  @GetMapping("/newStudent")
  public String newStudent(Model model) {

    StudentDetail detail = new StudentDetail();

    detail.getStudentCourses().add(new StudentCourse());

    model.addAttribute("studentDetail", detail);

    return "registerStudent";
  }

  /**
   * 登録処理
   */
  @PostMapping("/students/register")
  public String registerStudent(
      @ModelAttribute("studentDetail") StudentDetail studentDetail
  ) {

    service.registerStudentWithCourses(
        studentDetail.getStudent(),
        studentDetail.getStudentCourses()
    );

    return "redirect:/studentsView";
  }
}