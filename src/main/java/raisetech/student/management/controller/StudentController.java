package raisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです
 */
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索を行います
   * 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(){
    return service.searchStudentList();
  }

  /**
   * 受講生検索を行います
   * IDに紐づく任意の受講生の情報を取得します
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable int id){
    return service.searchStudent(id);
  }

  /**
   * 受講生登録を行います
   *
   * @param studentDetail 登録する受講生の詳細情報
   * @return 登録された受講生の詳細情報
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生の更新を行います
   * @param studentDetail 更新する受講生の詳細情報
   * @return 更新された受講生の詳細情報
   */
  @PostMapping("/updateStudent")
  public ResponseEntity<StudentDetail> updateStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail updateStudentDetail = service.updateStudent(studentDetail);
    return ResponseEntity.ok(updateStudentDetail);
  }

}