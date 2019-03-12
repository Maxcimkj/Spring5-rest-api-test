package com.mekh.spring.university.api.services.impl;

import com.mekh.spring.university.api.dao.StudentRepository;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;
import com.mekh.spring.university.api.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository repository;

    public List<Student> getAll() {
        return repository.findAll();
    }

    public List<Student> getAllStudentsByFacultyId(Integer facultyId) {
        return repository.findAllByFacultyId(facultyId);
    }

    public Student findStudentById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Integer save(Faculty faculty, Student student) {
        student.setFaculty(faculty);
        return repository.save(student).getId();
    }

    public Integer update(Faculty faculty, Student student) {
        student.setFaculty(faculty);
        return repository.save(student).getId();
    }

    public void remove(Integer id) {
        repository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return repository.existsById(id);
    }
}
