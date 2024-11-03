package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生情報を取り扱うサービスです
 * 受講生の検索や登録・更新処理を行います
 */
@Service
public class StudentService {
  private final StudentRepository repository;
  private final StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です
   * 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生一覧(全件)
   */
  public List<StudentDetail> searchStudentList(){
    List<Student> studentList = repository.searchStudents();
    List<StudentsCourses> studentsCoursesList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生検索です
   * IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コースを取得して設定します
   *
   * @param id　受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(int id){
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(
        Integer.parseInt(student.getId()));
    return new StudentDetail(student, studentsCourses);
  }

  /**
   * 受講生の新規登録を行います
   * 受講生情報と一緒に、その受講生のコース情報登録も行います
   *
   * @param studentDetail 登録する受講生の詳細情報
   * @return 登録された受講生の詳細情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail){
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourses:studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      studentsCourses.setStartDate(LocalDateTime.now());
      studentsCourses.setEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourses);
    }
    return studentDetail;
  }

  /**
   * 受講生の更新処理を行います
   * 受講生情報と一緒に、その受講生のコース情報更新処理も行います
   * @param studentDetail 更新する受講生の詳細情報
   * @return 更新された受講生の詳細情報
   */
  @Transactional
  public StudentDetail updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourses: studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentsCourses(studentsCourses);
    }
    return studentDetail;
  }

}