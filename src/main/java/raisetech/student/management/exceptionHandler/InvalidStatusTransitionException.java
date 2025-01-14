package raisetech.student.management.exceptionHandler;

public class InvalidStatusTransitionException extends RuntimeException {

  public InvalidStatusTransitionException(String message) {
    super(message);
  }
}
