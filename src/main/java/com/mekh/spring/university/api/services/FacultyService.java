package com.mekh.spring.university.api.services;

import com.mekh.spring.university.api.model.Faculty;

import java.util.List;

public interface FacultyService {
    List<Faculty> getAllFaculties();

    Faculty getFacultyById(Integer id);

    Integer save(Faculty faculty);

    Integer update(Faculty faculty);

    void remove(Integer id);

    boolean exists(Integer id);
}
