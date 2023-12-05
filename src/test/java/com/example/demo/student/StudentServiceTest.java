package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;
  private StudentService service;

  @BeforeEach
  void setUp() {
    service = new StudentService(studentRepository);
  }

  @Test
  void canGetAllStudents() {
    //when
    service.getAllStudents();
    //then
    verify(studentRepository).findAll();
  }


  @Test
  void canAddStudent() {
    // given
    Student student = new Student(
            "Jamila",
            "jamila@gmail.com",
            Gender.FEMALE
    );
    // when
    service.addStudent(student);

    // then
    ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
    verify(studentRepository).save(studentArgumentCaptor.capture());
    Student capturedStudent = studentArgumentCaptor.getValue();

    assertThat(capturedStudent).isEqualTo(student);
  }

  @Test
  void willThrowWhenEmailsTaken() {
    // given
    Student student = new Student(
            "Jamila",
            "jamila@gmail.com",
            Gender.FEMALE
    );

    given(studentRepository.selectExistsEmail(student.getEmail()))
            .willReturn(true);
    // when

    // then
    assertThatThrownBy(() -> service.addStudent(student))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Email " + student.getEmail() + " taken");

    verify(studentRepository, never()).save(any());
  }

  @Test
  void canDeleteStudent() {
   // given
    long id = 10;
    given(studentRepository.existsById(id)).willReturn(true);

    // when
    service.deleteStudent(id);

    // then
    verify(studentRepository).deleteById(id);
  }

  @Test
  void willThrowWhenDeleteStudentNotFound() {
    // given
    long id = 10;
    given(studentRepository.existsById(id)).willReturn(false);

    // then
    assertThatThrownBy(() -> service.deleteStudent(id))
            .isInstanceOf(StudentNotFoundException.class)
            .hasMessageContaining("Student with id " + id + " does not exists");

    verify(studentRepository, never()).deleteById(any());
  }
}