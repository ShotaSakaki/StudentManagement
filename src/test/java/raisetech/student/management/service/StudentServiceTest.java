package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
  void searchTest(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void searchStudentTest(){
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
  void registerStudentTest(){
    Student mockStudent = new Student();
    StudentCourse course1 = new StudentCourse();
    StudentCourse course2 = new StudentCourse();
    List<StudentCourse> courseList = List.of(course1, course2);

    StudentDetail mockStudentDetail = new StudentDetail(mockStudent, courseList);

    doNothing().when(repository).registerStudent(mockStudent);
    doNothing().when(repository).registerStudentCourse(any(StudentCourse.class));

    StudentDetail result = sut.registerStudent(mockStudentDetail);

    assertNotNull(result);
    assertEquals(mockStudentDetail, result);

    verify(repository, times(1)).registerStudent(mockStudent);
    verify(repository, times(2)).registerStudentCourse(any(StudentCourse.class));

    courseList.forEach(course
        -> assertEquals(mockStudent.getId(), course.getStudentId()));
  }

  @Test
  void initStudentsCourseTest(){
    String studentId = "123";
    Student student = new Student();
    student.setId(studentId);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student);

    assertEquals(studentId, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getStartDate().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentCourse.getEndDate().getYear());
  }

  @Test
  void updateStudentTest(){
    Student mockStudent = new Student();
    StudentCourse course1 = new StudentCourse();
    StudentCourse course2 = new StudentCourse();
    List<StudentCourse> courseList = List.of(course1, course2);

    StudentDetail mockStudentDetail = new StudentDetail(mockStudent, courseList);

    doNothing().when(repository).updateStudent(mockStudent);
    doNothing().when(repository).updateStudentCourse(any(StudentCourse.class));

    sut.updateStudent(mockStudentDetail);

    verify(repository, times(1)).updateStudent(mockStudent);

    verify(repository, times(2)).updateStudentCourse(any(StudentCourse.class));
    verify(repository, times(1)).updateStudentCourse(course1);
    verify(repository, times(1)).updateStudentCourse(course2);
  }

}