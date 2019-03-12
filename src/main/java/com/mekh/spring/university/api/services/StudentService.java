package com.mekh.spring.university.api.services;

import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAll();

    List<Student> getAllStudentsByFacultyId(Integer facultyId);

    Student findStudentById(Integer id);

    Integer save(Faculty faculty, Student student);

    Integer update(Faculty faculty, Student student);

    void remove(Integer id);

    boolean exists(Integer id);
}
