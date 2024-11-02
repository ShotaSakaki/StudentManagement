package raisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model){
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    model.addAttribute("studentList",converter.convertStudentDetails(students,studentsCourses));
    return "studentList";
  }

  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable("id") int id, Model model){
    StudentDetail studentDetail = service.searchStudent(id);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model){
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourses(List.of(new StudentsCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute("studentDetail") StudentDetail studentDetail, BindingResult result){
    if(result.hasErrors()){
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    return "redirect:/studentList";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute("studentDetail") StudentDetail studentDetail, BindingResult result){
    if (result.hasErrors()){
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    return  "redirect:/studentList";
  }

}