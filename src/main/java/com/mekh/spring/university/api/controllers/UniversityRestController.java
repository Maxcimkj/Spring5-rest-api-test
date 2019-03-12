package com.mekh.spring.university.api.controllers;

import com.mekh.spring.university.api.exceptions.NotFoundException;
import com.mekh.spring.university.api.exceptions.ResourceExistsException;
import com.mekh.spring.university.api.exceptions.SystemErrorException;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;
import com.mekh.spring.university.api.services.FacultyService;
import com.mekh.spring.university.api.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/university/api")
public class UniversityRestController {
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/faculties/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Faculty> getAllFaculties() throws Exception {
        try {
            return facultyService.getAllFaculties();
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @GetMapping("/faculties/{facultyId}")
    @ResponseStatus(HttpStatus.OK)
    public Faculty getFaculty(@PathVariable Integer facultyId) throws Exception {
        Faculty faculty;
        try {
            faculty = facultyService.getFacultyById(facultyId);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }

        if (faculty == null)
            throw new NotFoundException("Faculty with sent id not found");

        return faculty;
    }

    @PostMapping("/faculties")
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) throws Exception {
        if (facultyService.exists(faculty.getId()))
            throw new ResourceExistsException("Faculty with sent id is exists");

        try {
            facultyService.save(faculty);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return faculty;
    }

    @PutMapping("/faculties")
    @ResponseStatus(HttpStatus.OK)
    public Faculty updateFaculty(@RequestBody Faculty faculty) throws Exception {
        try {
            facultyService.save(faculty);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return faculty;
    }

    @DeleteMapping("/faculties/{facultyId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFaculty(@PathVariable Integer facultyId) throws Exception {
        try {
            facultyService.remove(facultyId);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @GetMapping("/faculties/{facultyId}/students/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Student> getAllStudents(@PathVariable Integer facultyId) throws Exception {
        if (!facultyService.exists(facultyId))
            throw new NotFoundException("Faculty with sent id not found");

        try {
            return studentService.getAllStudentsByFacultyId(facultyId);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    @PostMapping("/faculties/{facultyId}/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@PathVariable Integer facultyId, @RequestBody Student student) throws Exception {
        Faculty faculty = facultyService.getFacultyById(facultyId);
        if (faculty == null)
            throw new NotFoundException("Faculty with sent id not found");

        if (studentService.exists(student.getId()))
            throw new ResourceExistsException("Student with sent id is exists");

        try {
            studentService.save(faculty, student);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return student;
    }

    @PutMapping("/faculties/{facultyId}/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student updateStudent(@PathVariable Integer facultyId, @RequestBody Student student) throws Exception {
        Faculty faculty = facultyService.getFacultyById(facultyId);

        if (faculty == null)
            throw new NotFoundException("Faculty with sent id not found");

        try {
            studentService.update(faculty, student);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
        return student;
    }

    @GetMapping("/students/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Student getStudent(@PathVariable Integer id) throws Exception {
        Student student;
        try {
            student = studentService.findStudentById(id);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }

        if (student == null)
            throw new NotFoundException("Student with sent id not found");

        return student;
    }

    @DeleteMapping("/students/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable Integer id) throws Exception {
        try {
            studentService.remove(id);
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }
}
