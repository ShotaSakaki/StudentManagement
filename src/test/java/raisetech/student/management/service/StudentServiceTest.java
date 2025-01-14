package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exceptionHandler.InvalidStatusTransitionException;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;
  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before(){
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバータの処理が適切に呼び出せていること(){
    String lastName = null;
    String firstName = null;
    String courseName = null;
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    String status = null;

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchWithConditions(lastName, firstName)).thenReturn(studentList);
    when(repository.searchStudentCourseListWithConditions(courseName, startDate, endDate, status)).thenReturn(studentCourseList);

    sut.searchStudentList(lastName, firstName, courseName, startDate, endDate, status);

    verify(repository, times(1)).searchWithConditions(lastName, firstName);
    verify(repository, times(1)).searchStudentCourseListWithConditions(courseName, startDate, endDate, status);
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細の単一検索_リポジトリの処理が適切に呼び出され期待通りの結果を返すこと(){
    String id = "123";
    Student student = new Student();
    student.setId(id);

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(new ArrayList<>());

    StudentDetail expected = new StudentDetail(student, new ArrayList<>());
    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository,times(1)).searchStudentCourse(id);
    assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @Test
  void 受講生登録処理_リポジトリの処理が適切に呼び出され期待通りの結果を返すこと(){
    Student mockstudent = new Student();
    mockstudent.setId("1");

    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockstudent, studentCourseList);

    doNothing().when(repository).registerStudent(mockstudent);
    doNothing().when(repository).registerStudentCourse(any(StudentCourse.class));

    LocalDateTime now = LocalDateTime.now();
    studentCourseList.forEach(course ->{
      course.setStudentId(mockstudent.getId());
      course.setStartDate(now);
      course.setEndDate(now.plusYears(1));
    });

    StudentDetail result = sut.registerStudent(mockstudentDetail);

    assertNotNull(result);
    assertEquals(mockstudentDetail, result);

    verify(repository, times(1)).registerStudent(mockstudent);
    verify(repository, times(1)).registerStudentCourse(any(StudentCourse.class));

    studentCourseList.forEach(course ->{
      assertEquals(mockstudent.getId(), course.getStudentId());
      assertNotNull(course.getStartDate());
      assertNotNull(course.getEndDate());
    });
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること(){
    String id = "123";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertEquals(id, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getStartDate().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentCourse.getEndDate().getYear());
  }

  @Test
  void 受講生情報更新処理_リポジトリの処理が適切に呼び出されること(){
    Student mockstudent = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail mockstudentDetail = new StudentDetail(mockstudent, studentCourseList);

    doNothing().when(repository).updateStudent(mockstudent);
    doNothing().when(repository).updateStudentCourse(any(StudentCourse.class));

    sut.updateStudent(mockstudentDetail);

    verify(repository, times(1)).updateStudent(mockstudent);


    verify(repository, times(1)).updateStudentCourse(any(StudentCourse.class));
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  void 新規コース登録時の初期ステータスが仮申込であることを確認できること(){
    StudentCourse course = new StudentCourse();
    sut.initStudentsCourse(course, "123");

    assertEquals("仮申込", course.getStatus());
    assertEquals("123", course.getStudentId());
    assertNotNull(course.getStartDate());
    assertNotNull(course.getEndDate());
  }

  @Test
  void ステータス更新の確認_正しい順序で更新されていること(){
    StudentCourse course = new StudentCourse();

    course.setStatus("仮申込");

    sut.updateCourseStatus(course, "本申込");
    assertEquals("本申込", course.getStatus());

    sut.updateCourseStatus(course, "受講中");
    assertEquals("受講中", course.getStatus());

    sut.updateCourseStatus(course, "受講終了");
    assertEquals("受講終了", course.getStatus());
  }

  @Test
  void 不正なステータスを指定した場合の例外処理(){
    StudentCourse course = new StudentCourse();
    course.setStatus("仮申込");

    InvalidStatusTransitionException exception = assertThrows(InvalidStatusTransitionException.class, () -> {
      sut.updateCourseStatus(course, "受講終了");
    });

    assertEquals("無効なステータス遷移: 仮申込->受講終了", exception.getMessage());

  }

}