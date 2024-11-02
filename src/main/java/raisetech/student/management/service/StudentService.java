package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;

@Service
public class StudentService {
  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){
    return repository.searchStudents();
  }

  public StudentDetail searchStudent(int id){
    Student student=repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(
        Integer.parseInt(student.getId()));
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
  }

  public List<StudentsCourses> searchStudentsCourseList(){
    return repository.searchStudentsCoursesList();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail){
    repository.registerStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourses:studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      studentsCourses.setStartDate(LocalDateTime.now());
      studentsCourses.setEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourses);
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    for (StudentsCourses studentsCourses: studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentsCourses(studentsCourses);
    }
  }

}