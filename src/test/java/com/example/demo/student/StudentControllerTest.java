package com.example.demo.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService studentService;

  @Autowired
  private ObjectMapper objectMapper;

  private Student student;

  @BeforeEach
  void init() {
    student = new Student(
            "Jamila",
            "jamila@gmail.com",
            Gender.FEMALE
    );
  }

  @Test
  void getAllStudents() throws Exception {
    ResultActions resultActions = mockMvc
            .perform(get("/api/v1/students")
                    .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void canAddNewStudentReturnCreated() throws Exception{
    // given
    given(studentService.addStudent(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(student))
    );
    // then
    resultActions.andExpect(status().isCreated());

  }

  @Test
  void canDeleteStudentReturnString() throws Exception{
    long id = 1;
    doNothing().when(studentService).deleteStudent(id);

    ResultActions resultActions = mockMvc
            .perform(delete("/api/v1/students/1")
                    .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }
}