package com.mekh.spring.university.api.dao;

import com.mekh.spring.university.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("select s from Student s where s.faculty.id = :facultyId")
    List<Student> findAllByFacultyId(@Param("facultyId") Integer facultyId);
}
