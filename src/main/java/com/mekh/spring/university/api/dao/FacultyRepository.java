package com.mekh.spring.university.api.dao;

import com.mekh.spring.university.api.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
}
