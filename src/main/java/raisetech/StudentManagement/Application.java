package raisetech.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("raisetech.StudentManagement.repository") // Mapper をスキャン
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
