package raisetch.Student.Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class Application {

	private Map<String, String> student = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// 初期値の設定（コンストラクタ）
	public Application() {
		student.put("name", "Sachiko Hara");
		student.put("age", "44");
	}

	// GET: 学生情報を取得
	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		return student.get("name") + " " + student.get("age") + " sai";
	}

	// POST: 名前と年齢を一緒に更新
	@PostMapping("/name")
	public void setStudentInfo(@RequestParam String name, @RequestParam String age) {
		student.put("name", name);
		student.put("age", age);
	}

	// POST: 名前だけを更新
	@PostMapping("/studentName")
	public void updateStudentName(@RequestParam String name) {
		student.put("name", name);
	}
}
