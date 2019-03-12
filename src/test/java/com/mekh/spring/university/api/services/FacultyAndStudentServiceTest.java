package com.mekh.spring.university.api.services;

import com.mekh.spring.university.api.DataAccessConfig;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataAccessConfig.class)
public class FacultyAndStudentServiceTest {
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;

    @Test
    public void facultyServiceCRUDOperationsTest() {
        //create test
        Faculty newFaculty = createFaculty("new_faculty");
        Integer id = facultyService.save(newFaculty);
        Faculty savedFaculty = facultyService.getFacultyById(id);
        Assert.assertNotNull(savedFaculty);
        Assert.assertEquals(id, (Integer) savedFaculty.getId());
        // exists test
        Assert.assertTrue(facultyService.exists(id));
        // update test
        String otherFacultyName = "other_faculty_name";
        newFaculty.setName(otherFacultyName);
        id = facultyService.update(newFaculty);
        Faculty updateFaculty = facultyService.getFacultyById(id);
        Assert.assertNotNull(updateFaculty);
        Assert.assertEquals(id, (Integer) updateFaculty.getId());
        Assert.assertEquals(otherFacultyName, updateFaculty.getName());
        //remove test
        facultyService.remove(id);
        Faculty removedFaculty = facultyService.getFacultyById(id);
        Assert.assertNull(removedFaculty);
        // get all test
        Faculty firstFaculty = createFaculty("first_faculty");
        facultyService.save(firstFaculty);
        Faculty secondFaculty = createFaculty("second_faculty");
        facultyService.save(savedFaculty);
        List<Faculty> allFaculties = facultyService.getAllFaculties();
        Assert.assertNotNull(allFaculties);
        Assert.assertEquals(allFaculties.size(), 2);
    }

    @Test
    public void studentServiceCRUDOperationsTest() {
        // create test
        Faculty faculty = createFaculty("new_faculty");
        Integer facultyId = facultyService.save(faculty);
        Student student = createStudent("new_student");
        Integer studentId = studentService.save(faculty, student);
        Student savedStudent = studentService.findStudentById(studentId);
        Assert.assertNotNull(savedStudent);
        Assert.assertEquals(studentId, (Integer) savedStudent.getId());
        Assert.assertEquals(facultyId, (Integer) savedStudent.getFaculty().getId());
        // exists test
        Assert.assertTrue(studentService.exists(studentId));
        // update test
        String otherName = "other_student_name";
        student.setName("other_student_name");
        Integer updatedStudentId = studentService.update(faculty, student);
        Student updatedStudent = studentService.findStudentById(studentId);
        Assert.assertNotNull(updatedStudent);
        Assert.assertEquals(otherName, updatedStudent.getName());
        // remove test
        studentService.remove(studentId);
        Student removedStudent = studentService.findStudentById(studentId);
        Assert.assertNull(removedStudent);
        facultyService.remove(facultyId);
    }

    private Faculty createFaculty(String name) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        return faculty;
    }

    private Student createStudent(String name) {
        Student student = new Student();
        student.setName(name);
        student.setBirthday(new Date());
        return student;
    }
}
