package raisetech.student.management.data;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {
  private String id;
  private String studentId;
  private String courseName;
  private Timestamp startDate;
  private Timestamp endDate;
}
