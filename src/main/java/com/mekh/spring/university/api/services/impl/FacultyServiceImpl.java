package com.mekh.spring.university.api.services.impl;

import com.mekh.spring.university.api.dao.FacultyRepository;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.services.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    FacultyRepository repository;

    public List<Faculty> getAllFaculties() {
        return repository.findAll();
    }

    public Faculty getFacultyById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Integer save(Faculty faculty) {
        return repository.save(faculty).getId();
    }

    public Integer update(Faculty faculty) {
        return repository.save(faculty).getId();
    }

    public void remove(Integer id) {
        repository.deleteById(id);
    }

    public boolean exists(Integer id) {
        return repository.existsById(id);
    }
}
