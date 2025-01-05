package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);

    assertThat(actual.get(0).getLastName()).isEqualTo("新庄");
    assertThat(actual.get(0).getFirstName()).isEqualTo("剛志");
    assertThat(actual.get(0).getLastNameFurigana()).isEqualTo("しんじょう");
    assertThat(actual.get(0).getFirstNameFurigana()).isEqualTo("つよし");
    assertThat(actual.get(0).getNickname()).isEqualTo("BIGBOSS");
    assertThat(actual.get(0).getEmail()).isEqualTo("fansareourtreasure@fighters.com");
    assertThat(actual.get(0).getPrefecture()).isEqualTo("北海道");
    assertThat(actual.get(0).getAge()).isEqualTo(52);
    assertThat(actual.get(0).getGender()).isEqualTo("男");
  }

  @Test
  void 受講生の新規登録が行えること(){
    Student student = createStudent();

    sut.registerStudent(student);

    List<Student> mockStudent = sut.search();

    assertThat(mockStudent.size()).isEqualTo(6);
  }

  @Test
  void 受講生をIDで検索できること(){
    Student actual = sut.searchStudent("1");
    assertThat(actual).isNotNull();
    assertThat(actual.getLastName()).isEqualTo("新庄");
    assertThat(actual.getFirstName()).isEqualTo("剛志");
    assertThat(actual.getLastNameFurigana()).isEqualTo("しんじょう");
    assertThat(actual.getFirstNameFurigana()).isEqualTo("つよし");
    assertThat(actual.getNickname()).isEqualTo("BIGBOSS");
    assertThat(actual.getEmail()).isEqualTo("fansareourtreasure@fighters.com");
    assertThat(actual.getPrefecture()).isEqualTo("北海道");
    assertThat(actual.getAge()).isEqualTo(52);
    assertThat(actual.getGender()).isEqualTo("男");
  }

  @Test
  void 存在しないIDで受講生を検索するとnullが返されること(){
    Student actual = sut.searchStudent("999");
    assertThat(actual).isNull();
  }

  @Test
  void 指定した条件で受講生を検索できること(){
    String lastName = "新庄";
    String firstName = "剛志";
    List<Student> result = sut.searchWithConditions(lastName, firstName);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getLastName()).isEqualTo(lastName);
    assertThat(result.get(0).getFirstName()).isEqualTo(firstName);
  }

  @Test
  void 検索条件にnullを渡しても全件が取得できること(){
    List<Student> actual = sut.searchWithConditions(null, null);
    assertThat(actual).hasSize(5);
  }

  @Test
  void 受講生のコース情報を全件検索できること(){
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);

    assertThat(actual.get(0).getStudentId()).isEqualTo("1");
    assertThat(actual.get(0).getCourseName()).isEqualTo("TOEIC");
    assertThat(actual.get(0).getStartDate()).isEqualTo(LocalDateTime.of(2024,4,1,0,0,0));
    assertThat(actual.get(0).getEndDate()).isEqualTo(LocalDateTime.of(2024,9,30,23,59,59));
  }

  @Test
  void 受講生IDで紐づくコース名を単一検索できること(){
    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getCourseName()).isEqualTo("TOEIC");
    assertThat(actual.get(0).getStartDate()).isEqualTo(LocalDateTime.of(2024,4,1,0,0,0));
    assertThat(actual.get(0).getEndDate()).isEqualTo(LocalDateTime.of(2024,9,30,23,59,59));
  }

  @Test
  void 指定した条件で受講生のコース情報を検索できること(){
    String courseName = "TOEIC";
    LocalDateTime startDate = LocalDateTime.of(2024,4,1,0,0,0);
    LocalDateTime endDate = LocalDateTime.of(2024,9,30,23,59,59);
    String status = "本申込";

    List<StudentCourse> result = sut.searchStudentCourseListWithConditions(courseName, startDate, endDate, status);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getCourseName()).isEqualTo(courseName);
    assertThat(result.get(0).getStartDate()).isEqualTo(startDate);
    assertThat(result.get(0).getEndDate()).isEqualTo(endDate);
  }

  @Test
  void 新しい受講生コース情報を登録できること(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("2");
    studentCourse.setCourseName("Java");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生情報を更新できること(){
    Student student = sut.searchStudent("4");
    assertThat(student).isNotNull();

    student.setFirstName("好花");
    sut.updateStudent(student);

    Student updatedStudent = sut.searchStudent("4");
    assertThat(updatedStudent.getFirstName()).isEqualTo("好花");
  }

  @Test
  void  受講生コース情報を更新できること(){
    List<StudentCourse> courses = sut.searchStudentCourse("1");
    assertThat(courses).isNotEmpty();

    StudentCourse course = courses.get(0);
    course.setCourseName("バイナリーオプショントレード");
    sut.updateStudentCourse(course);

    List<StudentCourse> updateCourses = sut.searchStudentCourse("1");
    assertThat(updateCourses.get(0).getCourseName()).isEqualTo("バイナリーオプショントレード");
  }

  private Student createStudent(){
    Student student = new Student();
    student.setLastName("新庄");
    student.setFirstName("剛志");
    student.setLastNameFurigana("しんじょう");
    student.setFirstNameFurigana("つよし");
    student.setNickname("BIGBOSS");
    student.setEmail("北海道");
    student.setAge(52);
    student.setGender("男");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

}