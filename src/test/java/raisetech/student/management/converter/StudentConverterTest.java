package raisetech.student.management.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

public class StudentConverterTest {
  private final StudentConverter studentConverter = new StudentConverter();

  @Test
  void  有効なデータを渡した場合に受講生詳細情報を正しく変換できること(){
    Student student1 = createStudent("1","新庄", "剛志", "しんじょう", "つよし",
        "BIGBOSS", "fansareourtreasure@fighters.com", "北海道", 52, "男", null, false);
    Student student2 = createStudent("2","遠藤", "さくら", "えんどう", "さくら",
        "さく", "sakuraendo_official@nogizaka46.com", "愛知県", 23, "女", null, false);
    List<Student> studentList = Arrays.asList(student1, student2);

    StudentCourse course1 = createStudentCourse("1", "1", "TOEIC", LocalDateTime.now(), LocalDateTime.now().plusYears(1));
    StudentCourse course2 = createStudentCourse("3", "2", "CAD", LocalDateTime.now(), LocalDateTime.now().plusYears(1));
    List<StudentCourse> studentCourseList = Arrays.asList(course1, course2);

    CourseStatus status1 = createCourseStatus("1", "1","本申込");
    CourseStatus status2 = createCourseStatus("2", "3", "受講中");

    List<CourseStatus> courseStatusList = Arrays.asList(status1, status2);

    List<StudentDetail> result = studentConverter.convertStudentDetails(studentList, studentCourseList, courseStatusList);

    assertThat(result).hasSize(2);

    StudentDetail detail1 = result.getFirst();
    assertThat(detail1.getStudent()).isEqualTo(student1);
    assertThat(detail1.getStudentCourseList()).containsExactlyInAnyOrder(course1);
    assertThat(course1.getCourseStatusList()).containsExactly(status1);

    StudentDetail detail2 = result.get(1);
    assertThat(detail2.getStudent()).isEqualTo(student2);
    assertThat(detail2.getStudentCourseList()).containsExactlyInAnyOrder(course2);
    assertThat(course2.getCourseStatusList()).containsExactly(status2);

  }

  @Test
  void 受講生のリストが空の場合に空の受講生詳細情報リストが返されること(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();

    List<StudentDetail> result = studentConverter.convertStudentDetails(studentList, studentCourseList, courseStatusList);

    assertThat(result).isEmpty();

  }

  @Test
  void 受講生に対応するコースが存在しない場合にコース情報が空になること(){
    Student student1 = createStudent("1","新庄", "剛志", "しんじょう", "つよし",
        "BIGBOSS", "fansareourtreasure@fighters.com", "北海道", 52, "男", null, false);
    List<Student> studentList = List.of(student1);

    StudentCourse course1 = createStudentCourse("1", "1", "TOEIC", LocalDateTime.now(), LocalDateTime.now().plusYears(1));
    List<StudentCourse> studentCourseList = List.of(course1);

    List<CourseStatus> courseStatusList = new ArrayList<>();

    List<StudentDetail> result = studentConverter.convertStudentDetails(studentList, studentCourseList, courseStatusList);

    assertThat(result).hasSize(1);

    StudentDetail detail1 = result.getFirst();
    assertThat(detail1.getStudent()).isEqualTo(student1);
    assertThat(detail1.getStudentCourseList().isEmpty());

  }

  public static Student createStudent(String id, String lastName, String firstName, String lastNameFurigana, String firstNameFurigana,
      String nickname, String email, String prefecture, int age, String gender, String remark, boolean isDeleted){
    Student student = new Student();
    student.setId(id);
    student.setLastName(lastName);
    student.setFirstName(firstName);
    student.setLastNameFurigana(lastNameFurigana);
    student.setFirstNameFurigana(firstNameFurigana);
    student.setNickname(nickname);
    student.setEmail(email);
    student.setPrefecture(prefecture);
    student.setAge(age);
    student.setGender(gender);
    student.setRemark(remark);
    student.setDeleted(isDeleted);
    return student;
  }

  public static StudentCourse createStudentCourse(String id, String studentId, String courseName,
      LocalDateTime startDate, LocalDateTime endDate) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(id);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName(courseName);
    studentCourse.setStartDate(startDate);
    studentCourse.setEndDate(endDate);
    return studentCourse;
  }

  public static CourseStatus createCourseStatus(String id, String studentCoursesId, String status) {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setId(id);
    courseStatus.setStudentCoursesId(studentCoursesId);
    courseStatus.setStatus(status);
    return courseStatus;
  }

}
