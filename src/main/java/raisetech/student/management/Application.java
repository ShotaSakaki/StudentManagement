package raisetech.student.management;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/webapp")
	public String webapp(){
		boolean isMixedCase = StringUtils.isMixedCase("目標は、Webアプリケーションが作れるようになること！");
		return "目標は、Webアプリケーションが作れるようになること！" + isMixedCase;
	}
}
