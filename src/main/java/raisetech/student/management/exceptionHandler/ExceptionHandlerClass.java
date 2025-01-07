package raisetech.student.management.exceptionHandler;

import java.util.Collections;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerClass{

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCourseNotFoundException(CourseNotFoundException ex) {
    Map<String, String> response = Collections.singletonMap("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidStatusTransitionException.class)
  public ResponseEntity<Map<String, String>> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
    Map<String, String> response = Collections.singletonMap("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

}
