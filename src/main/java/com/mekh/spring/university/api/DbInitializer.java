package com.mekh.spring.university.api;

import com.mekh.spring.university.api.dao.FacultyRepository;
import com.mekh.spring.university.api.dao.StudentRepository;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class DbInitializer {
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostConstruct
    public void initDb() {
        Faculty firstFaculty = createFaculty("first_faculty");
        Faculty secondFaculty = createFaculty("second_faculty");
        facultyRepository.save(firstFaculty);
        facultyRepository.save(secondFaculty);

        Student firstStudent = createStudent("first_student", firstFaculty);
        Student secondStudent = createStudent("second_student", firstFaculty);
        Student thirdStudent = createStudent("third_student", secondFaculty);
        Student fourthStudent = createStudent("fourth_student", secondFaculty);
        studentRepository.save(firstStudent);
        studentRepository.save(secondStudent);
        studentRepository.save(thirdStudent);
        studentRepository.save(fourthStudent);
    }

    private Faculty createFaculty(String name) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        return faculty;
    }

    private Student createStudent(String name, Faculty faculty) {
        Student student = new Student();
        student.setName(name);
        student.setBirthday(new Date());
        student.setFaculty(faculty);
        return student;
    }
}
