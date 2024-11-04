package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

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
  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  /**
   * 受講生の検索を行います
   *
   * @param id 受講生ID
   * @return 受講生の詳細情報
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(int id);

  /**
   * 受講生のコース情報の全件検索を行います
   *
   * @return 受講生のコース情報(全件)
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します
   *
   * @param studentId　受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCourse(int studentId);

  /**
   * 受講生を新規登録します
   * IDに関しては自動採番を行います
   *
   * @param student 受講生
   */
  @Insert(
      "INSERT INTO students(last_name, first_name, last_name_furigana, first_name_furigana, nickname, email, prefecture, age, gender, remark, is_deleted)"
          + " VALUES(#{lastName}, #{firstName}, #{lastNameFurigana}, #{firstNameFurigana}, #{nickname}, #{email}, #{prefecture}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します
   * IDに関しては自動採番を行います
   *
   * @param studentCourse 受講生コース情報
   */
  @Insert("INSERT INTO students_courses(student_id, course_name, start_date, end_date) "
      + " VALUES(#{studentId}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します
   *
   * @param student 受講生
   */
  @Update(
      "UPDATE students SET last_name = #{lastName}, first_name = #{firstName}, last_name_furigana = #{lastNameFurigana},  first_name_furigana = #{firstNameFurigana}, nickname = #{nickname}, email = #{email}, prefecture = #{prefecture}, age = #{age},"
          + " gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します
   *
   * @param studentCourse 受講生コース情報
   */
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentCourse(StudentCourse studentCourse);

}