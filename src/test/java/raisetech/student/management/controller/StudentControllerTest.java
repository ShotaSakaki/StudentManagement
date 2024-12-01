package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void studentListTest() throws Exception{
     mockMvc.perform(get("/studentList")).andExpect(status().isOk()).andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void studentIdTest() throws Exception{
    String id = "123";
    mockMvc.perform(get("/student/{id}", id)).andExpect(status().isOk());
    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void registerTest() throws Exception{
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
        """
            {
                    "student": {
                        "id": "2",
                        "lastName": "遠藤",
                        "firstName": "さくら",
                        "lastNameFurigana": "えんどう",
                        "firstNameFurigana": "さくら",
                        "nickname": "さく",
                        "email": "sakuraendo_official@nogizaka46.com",
                        "prefecture": "愛知県",
                        "age": 23,
                        "gender": "女",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "courseName": "CAD"
                        }
                    ]
                }
            """
    )).andExpect(status().isOk());
    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void updateTest() throws Exception{
mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
    """
        {
            "student": {
                "id": "1",
                "lastName": "新庄",
                "firstName": "剛志",
                "lastNameFurigana": "しんじょう",
                "firstNameFurigana": "つよし",
                "nickname": "BIGBOSS",
                "email": "fansareourtreasure@fighters.com",
                "prefecture": "北海道",
                "age": 52,
                "gender": "男",
                "remark": "",
                "deleted": false
            },
            "studentCourseList": [
                {
                "id": "7",
                "studentId": "1",
                "courseName": "CAD",
                "startDate": "2024-10-01T00:00:00",
                "endDate": "2025-03-31T23:59:59"
                }
            ]
        }
        """
)).andExpect(status().isOk());
verify(service, times(1)).updateStudent(any());
  }

  @Test
  void studentListExceptionTest() throws Exception{
    mockMvc.perform(get("/studentListException"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("全件検索は http://localhost:8080/studentList を使用してください"));
  }

  @Test
  void inputErrorCheckTest(){
    Student student = new Student();
    student.setId("1");
    student.setLastName("新庄");
    student.setFirstName("剛志");
    student.setLastNameFurigana("しんじょう");
    student.setFirstNameFurigana("つよし");
    student.setEmail("fansareourtreasure@fighters.com");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void inputCheckTest(){
    Student student = new Student();
    student.setId("テストです");
    student.setLastName("新庄");
    student.setFirstName("剛志");
    student.setLastNameFurigana("しんじょう");
    student.setFirstNameFurigana("つよし");
    student.setEmail("fansareourtreasure@fighters.com");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("数字のみを入力するようにしてください");
  }
}