package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exceptionHandler.TestException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです
 */
@Validated
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索を行います
   * 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生詳細一覧(全件)
   */
  @Operation(tags = "全件検索", summary = "受講生一覧検索", description = "受講生の一覧を検索します",
      responses = {@ApiResponse(responseCode = "200", description = "受講生の詳細が一覧で出力される")
      })
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  @Operation(tags = "例外処理", summary = "受講生一覧検索(例外処理)", description = "受講生の一覧を検索します(URLが間違っている場合の処理)",
      responses = {@ApiResponse(responseCode = "200", description = "「全件検索は http://localhost:8080/studentList を使用してください」と表示される")
      })
  @GetMapping("/studentListException")
  public List<StudentDetail> getStudentListException() throws TestException{
    throw new TestException("全件検索は http://localhost:8080/studentList を使用してください");
  }

  /**
   * 受講生詳細の検索を行います
   * IDに紐づく任意の受講生の情報を取得します
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(tags = "検索", summary = "受講生検索", description = "IDに紐づく任意の受講生の情報を取得します",
      responses = {@ApiResponse(responseCode = "200", description = "IDに紐づく任意の受講生の情報が出力される")
      })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @PathVariable
      @NotBlank
      @Pattern(regexp = "^\\d+$", message = "IDは数字を指定してください") String id){
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(tags = "登録", summary = "受講生登録", description = "受講生を登録します",
      responses = {@ApiResponse(responseCode = "200", description = "登録された情報が出力される")
      })
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生情報の更新を行います
   * キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(tags = "更新", summary = "受講生情報更新", description = "受講生情報を更新します。キャンセルフラグの更新もここで行います(論理削除)",
      responses = {@ApiResponse(responseCode = "200", description = "更新されたの受講生の情報が出力される")
      })
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail){
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理に成功しました");
  }

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

}