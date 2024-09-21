package raisetech.student.management.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.Student;
import raisetech.student.management.StudentRepository;
import raisetech.student.management.StudentsCourses;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){

    List<Student> students = repository.searchStudents();
    List<Student> filterdStudents = new ArrayList<>();

    for (Student student : students){

      if (student.getAge() >= 30 && student.getAge() < 40){
        filterdStudents.add(student);
      }

    }
    return filterdStudents;

  }

  public List<StudentsCourses> searchStudentsCourseList(){

    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses();
    List<StudentsCourses> filterdStudentsCourses = new ArrayList<>();

    for (StudentsCourses courses : studentsCourses){
      if ("Java".equals(courses.getCourseName())){
        filterdStudentsCourses.add(courses);
      }
    }
    return filterdStudentsCourses;

  }

}
