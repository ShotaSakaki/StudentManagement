package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.data.StudentsCourses;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){

    return repository.searchStudents();

  }

  public List<StudentsCourses> searchStudentsCourseList(){
    return repository.searchStudentsCourses();
  }

}
