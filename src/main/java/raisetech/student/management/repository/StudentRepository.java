package raisetech.student.management.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.CourseStatus;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います
   *
   * @return 受講生一覧(全件)
   */
  List<Student> search();

  /**
   * 受講生の単一検索を行います
   *
   * @param id 受講生ID
   * @return 受講生の詳細情報
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います
   *
   * @return 受講生のコース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します
   *
   * @param studentId　受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 指定された条件に一致する受講生を検索します
   * @param lastName 受講生の名字(部分一致検索)。 nullの場合、条件に含めません
   * @param firstName 受講生の名前(部分一致検索)。 nullの場合、条件に含めません
   * @return 検索結果に一致する受講生のリスト
   */
  List<Student> searchWithConditions(@Param("lastName") String lastName, @Param("firstName") String firstName);

  /**
   * 指定した条件に一致する受講生コース情報を検索します
   * @param courseName コース名(部分一致検索)。 nullの場合、条件に含めません
   * @param startDate コース開始日(指定された日付以降)。 nullの場合、条件に含めません
   * @param endDate コース終了日(指定された日付以前)。 nullの場合、条件に含めません
   * @param status コースのステータス(完全一致検索)。 nullの場合、条件に含めません
   * @return 検索結果に一致する受講生コース情報のリスト
   */
  List<StudentCourse> searchStudentCourseListWithConditions(
      String courseName,
      LocalDateTime startDate,
      LocalDateTime endDate,
      String status
  );

  /**
   * 条件に基づいてCourseStatusリストを検索します
   *
   * @param status 検索するコースの申込状態
   * @return 条件に一致するCourseStatusのリスト
   */
  List<CourseStatus> searchCourseStatusListWithConditions(@Param("status") String status);

  /**
   * 受講生IDに基づき、受講生の受講したコースの申込状態を検索します
   *
   * @param studentId 受講生ID
   * @return 受講生IDに基づいて関連するCourseStatusのリスト
   */
  List<CourseStatus> searchCourseStatusByStudentId(@Param("studentId") String studentId);

  /**
   * 受講生を新規登録します
   * IDに関しては自動採番を行います
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します
   * IDに関しては自動採番を行います
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 新しいCourseStatusを登録します
   *
   * @param courseStatus コースの申込状態
   */
  void registerCourseStatus(CourseStatus courseStatus);

  /**
   * 指定されたコースIDに基づき、最新のCourseStatusを検索します
   *
   * @param courseId コースID
   * @return 指定されたコースIDに関連する最新のCourseStatus
   */
  CourseStatus findLatestCourseStatusByCourseId(@Param("courseId") String courseId);

  /**
   * 受講生を更新します
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 既存のCourseStatusを更新します
   *
   * @param courseStatus 既存のCourseStatus。既存のCourseStatusのIDと一致するレコードが更新されます
   */
  void updateCourseStatus(CourseStatus courseStatus);

}