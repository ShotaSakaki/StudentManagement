package raisetech.student.management.exceptionHandler;

public class CourseNotFoundException extends RuntimeException {

  public CourseNotFoundException(String message) {
    super(message);
  }
}
