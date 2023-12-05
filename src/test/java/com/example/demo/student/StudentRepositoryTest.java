package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository repositoryTest;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
    repositoryTest.deleteAll();
  }

  @Test
  void itShouldCheckWhenStudentExistsEmail() {
    // give
    String mail = "jamila@gmail.com";
    Student student = new Student(
            "Jamila",
            mail,
            Gender.FEMALE
    );
    repositoryTest.save(student);

    // when
    Boolean expected = repositoryTest.selectExistsEmail(mail);

    // then
    assertThat(expected).isTrue();

  }

  @Test
  void itShouldCheckWhenStudentEmailDoesNotExists() {
    // give
    String mail = "jamila@gmail.com";

    // when
    Boolean expected = repositoryTest.selectExistsEmail(mail);

    // then
    assertThat(expected).isFalse();

  }
}