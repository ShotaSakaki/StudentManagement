package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
   * 受講生詳細の一覧検索です
   * 条件指定ができます。条件が指定されない場合は全件検索を行います
   * @param lastName 受講生の名字(部分一致検索)
   * @param firstName 受講生の名前(部分一致検索)
   * @param courseName コース名(部分一致検索)
   * @param startDate コース開始日(指定された日付以降)
   * @param endDate コース終了日(指定された日付以前)
   * @param status コースのステータス(指定されたステータス)
   * @return 受講生詳細一覧(条件に一致するもの、または全件)
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を条件付きで検索します",
      responses = {@ApiResponse(responseCode = "200", description = "条件に一致する受講生の情報、または全受講生の詳細が出力される")
  })
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentListWithConditions(
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "courseName", required = false) String courseName,
      @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDate,
      @RequestParam(value = "status", required = false) String status){
    return service.searchStudentList(lastName, firstName, courseName, startDate, endDate, status);
  }

  @Operation(summary = "一覧検索(例外処理)", description = "受講生の一覧検索(エラー)")
  @GetMapping("/studentListException")
  public List<StudentDetail> getStudentListException() throws TestException{
    throw new TestException("エラーが発生しました");
  }

  /**
   * 受講生詳細の検索を行います
   * IDに紐づく任意の受講生の情報を取得します
   *
   * @param id 受講生ID
   * @return 受講生情報
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

  @Operation(summary = "コースステータス更新", description = "受講生のコースステータスを更新します")
  @PutMapping("/updateCourseStatus/{courseId}")
  public ResponseEntity<String> updateCourseStatus(
      @PathVariable String courseId,
      @RequestParam @NotBlank String status){
    try {
      service.updateCourseStatus(courseId, status);
      return ResponseEntity.ok("コースステータスを更新しました");
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().body("エラー: " + e.getMessage());
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("エラー: 指定されたコースが見つかりません");
    }

  }

}