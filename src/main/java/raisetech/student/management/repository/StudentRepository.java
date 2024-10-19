package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ
 * 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */

@Mapper
public interface StudentRepository {

  /**
   * 全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */

  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  @Select("SELECT * FROM students WHERE student_id=#{studentId}")
  Student searchStudent(int studentId);

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCoursesList();

  @Select("SELECT * FROM students_courses WHERE student_id=#{studentId}")
  List<StudentsCourses> searchStudentsCourses(int studentId);

  @Insert("INSERT INTO students(full_name, furigana, nickname, email, prefecture, age, gender, remark, is_deleted)"
      + " VALUES(#{fullName}, #{furigana}, #{nickname}, #{email}, #{prefecture}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses(student_id, course_name, start_date, end_date) "
      + " VALUES(#{studentId}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentsCourses(StudentsCourses studentsCourses);

  @Update("UPDATE students SET full_name=#{fullName},  furigana=#{furigana}, nickname=#{nickname}, email=#{email}, prefecture=#{prefecture}, age=#{age},"
      + " gender=#{gender}, remark=#{remark}, is_deleted=#{isDeleted} WHERE student_id=#{studentId}")
    void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE course_id = #{courseId}")
  void updateStudentsCourses(StudentsCourses studentsCourses);

}