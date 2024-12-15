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

    assertThat(actual.getFirst().getLastName()).isEqualTo("新庄");
    assertThat(actual.getFirst().getFirstName()).isEqualTo("剛志");
    assertThat(actual.getFirst().getLastNameFurigana()).isEqualTo("しんじょう");
    assertThat(actual.getFirst().getFirstNameFurigana()).isEqualTo("つよし");
    assertThat(actual.getFirst().getNickname()).isEqualTo("BIGBOSS");
    assertThat(actual.getFirst().getEmail()).isEqualTo("fansareourtreasure@fighters.com");
    assertThat(actual.getFirst().getPrefecture()).isEqualTo("北海道");
    assertThat(actual.getFirst().getAge()).isEqualTo(52);
    assertThat(actual.getFirst().getGender()).isEqualTo("男");
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
  void 受講生のコース情報を全件検索できること(){
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);

    assertThat(actual.getFirst().getStudentId()).isEqualTo("1");
    assertThat(actual.getFirst().getCourseName()).isEqualTo("TOEIC");
    assertThat(actual.getFirst().getStartDate()).isEqualTo(LocalDateTime.of(2024, 4, 1, 0, 0,0));
    assertThat(actual.getFirst().getEndDate()).isEqualTo(LocalDateTime.of(2024, 9, 30, 23, 59,59));
  }

  @Test
  void 受講生IDで紐づくコース名を検索できること(){
    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.size()).isEqualTo(1);

    assertThat(actual.getFirst().getStudentId()).isEqualTo("1");
    assertThat(actual.getFirst().getCourseName()).isEqualTo("TOEIC");
    assertThat(actual.getFirst().getStartDate()).isEqualTo(LocalDateTime.of(2024, 4, 1, 0, 0,0));
    assertThat(actual.getFirst().getEndDate()).isEqualTo(LocalDateTime.of(2024, 9, 30, 23, 59,59));
  }

  @Test
  void 受講生の登録が行えること(){
    Student student = new Student();
    student.setLastName("新庄");
    student.setFirstName("剛志");
    student.setLastNameFurigana("しんじょう");
    student.setFirstNameFurigana("つよし");
    student.setNickname("BIGBOSS");
    student.setEmail("fansareourtreasure@fighters.com");
    student.setPrefecture("北海道");
    student.setAge(52);
    student.setGender("男");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報を登録できること(){
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
    Student student = new Student();
    student.setId("4");
    student.setLastName("松田");
    student.setFirstName("好花");
    student.setLastNameFurigana("まつだ");
    student.setFirstNameFurigana("このか");
    student.setNickname("だーこの");
    student.setEmail("aaa@hinatazaka46.com");
    student.setPrefecture("京都府");
    student.setAge(25);
    student.setGender("女");
    student.setRemark("");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student actual = sut.searchStudent("4");

    assertThat(actual.getLastName()).isEqualTo("松田");
  }

  @Test
  void  受講生コース情報を更新できること(){
    List<StudentCourse> courses = sut.searchStudentCourse("4");
    assertThat(courses).isNotEmpty();

    StudentCourse course = courses.getFirst();
    course.setCourseName("バイナリーオプショントレード");
    sut.updateStudentCourse(course);

    List<StudentCourse> updateCourses = sut.searchStudentCourse("4");
    assertThat(updateCourses.getFirst().getCourseName()).isEqualTo("バイナリーオプショントレード");
  }

}