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

/**
 * 受講生の画面操作（画面遷移・フォーム送信）を扱うコントローラークラス
 * 主にThymeleafなどのテンプレートと連携する。
 */
@Controller
public class StudentViewController {

  private final StudentService service;

  public StudentViewController(StudentService service) {
    this.service = service;
  }

  /**
   * 新規登録画面を表示する
   *
   * @param model 画面にデータを渡すためのモデル
   * @return 新規登録画面のテンプレート名
   */
  @GetMapping("/newStudent")
  public String newStudent(Model model) {

    StudentDetail detail = new StudentDetail();

    detail.getStudentCourses().add(new StudentCourse());

    model.addAttribute("studentDetail", detail);

    return "registerStudent";
  }

  /**
   * 受講生の登録処理を行う
   *
   * @param studentDetail フォームから送信された受講生情報
   * @return 一覧画面へリダイレクト
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