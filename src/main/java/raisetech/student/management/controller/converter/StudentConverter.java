package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです
 */
@Component
public class StudentConverter {
  /**
   * 受講生に紐づく受講生情報をマッピングします
   * 受講生コース情報は受講生に対して複数存在するので、ループを回して受講生詳細情報を組み立てます
   *
   * @param studentList 受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @param courseStatusList 受講生詳細情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(
      List<Student> studentList,
      List<StudentCourse> studentCourseList,
      List<CourseStatus> courseStatusList) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .map(studentCourse -> {
            List<CourseStatus> convertCourseStatusList = courseStatusList.stream()
                .filter(courseStatus -> studentCourse.getId().equals(courseStatus.getStudentCoursesId()))
                .collect(Collectors.toList());
            studentCourse.setCourseStatusList(convertCourseStatusList);
            return studentCourse;
          })
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });

    return studentDetails;
  }

}