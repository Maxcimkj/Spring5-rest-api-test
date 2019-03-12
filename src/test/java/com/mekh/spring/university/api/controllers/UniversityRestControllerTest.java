package com.mekh.spring.university.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekh.spring.university.api.DataAccessConfig;
import com.mekh.spring.university.api.WebConfig;
import com.mekh.spring.university.api.model.Faculty;
import com.mekh.spring.university.api.model.Student;
import com.mekh.spring.university.api.services.FacultyService;
import com.mekh.spring.university.api.services.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, UniversityRestControllerTest.TestDataAccessConfig.class})
public class UniversityRestControllerTest {
    @Configuration
    @EnableTransactionManagement
    @PropertySource("classpath:db/jdbc-h2-configuration.properties")
    @EnableJpaRepositories(basePackages = {"com.mekh.spring.university.api.dao"})
    static class TestDataAccessConfig extends DataAccessConfig {
        @Bean
        public FacultyService facultyService() {
            return Mockito.mock(FacultyService.class);
        }

        @Bean
        public StudentService studentService() {
            return Mockito.mock(StudentService.class);
        }
    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;

    @Test
    public void getAllFacultiesTest() throws Exception {
        Faculty first = createFaculty(1, "first_faculty");
        Faculty second = createFaculty(2, "second_faculty");

        when(facultyService.getAllFaculties()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/university/api/faculties/all")
                .header("Accept", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].name", is(first.getName())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].name", is(second.getName())));

        mockMvc.perform(get("/university/api/faculties/all")
                .header("Accept", "application/xml"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("List/item[1]/id").string(is("1")))
                .andExpect(xpath("List/item[1]/name").string(is(first.getName())))
                .andExpect(xpath("List/item[2]/id").string(is("2")))
                .andExpect(xpath("List/item[2]/name").string(is(second.getName())));
    }

    @Test
    public void getStudentByFacultyTest() throws Exception {
        Student firstStudent = createStudent(1, "first_student");
        Student secondStudent = createStudent(2, "second_student");

        when(facultyService.exists(1)).thenReturn(true);
        when(studentService.getAllStudentsByFacultyId(1)).thenReturn(Arrays.asList(firstStudent, secondStudent));

        mockMvc.perform(get(String.format("/university/api/faculties/%d/students/all", 1))
                .header("Accept", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(firstStudent.getId())))
                .andExpect(jsonPath("$[0].name", is(firstStudent.getName())))
                .andExpect(jsonPath("$[1].id", is(secondStudent.getId())))
                .andExpect(jsonPath("$[1].name", is(secondStudent.getName())));

        mockMvc.perform(get(String.format("/university/api/faculties/%d/students/all", 1))
                .header("Accept", "application/xml"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("List/item[1]/id").string(is(Integer.toString(firstStudent.getId()))))
                .andExpect(xpath("List/item[1]/name").string(is(firstStudent.getName())))
                .andExpect(xpath("List/item[2]/id").string(is(Integer.toString(secondStudent.getId()))))
                .andExpect(xpath("List/item[2]/name").string(is(secondStudent.getName())));
    }

    @Test
    public void getFacultyByIdNotFoundExceptionTest() throws Exception {
        when(facultyService.getFacultyById(anyInt())).thenReturn(null);

        mockMvc.perform(get(String.format("/university/api/faculties/%d", 1))
                .header("Accept", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void saveStudentTest() throws Exception {
        Faculty faculty = createFaculty(1, "faculty");
        Student student = createStudent(1, "student");

        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(studentService.exists(any())).thenReturn(false);
        when(studentService.save(any(), any())).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);
        mockMvc.perform(post(String.format("/university/api/faculties/%d/students", 1))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .header("Accept", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(student.getId())))
                .andExpect(jsonPath("$.name", is(student.getName())));

        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(student);
        mockMvc.perform(post(String.format("/university/api/faculties/%d/students", 1))
                .contentType(MediaType.APPLICATION_XML).content(xml)
                .header("Accept", "application/xml"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(xpath("Student/id").string(is(Integer.toString(student.getId()))))
                .andExpect(xpath("Student/name").string(is(student.getName())));
    }

    @Test
    public void saveSameStudentExceptionTest() throws Exception {
        Faculty faculty = createFaculty(1, "faculty");
        Student student = createStudent(1, "student");

        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(studentService.exists(any())).thenReturn(true);
        when(studentService.save(any(), any())).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);
        mockMvc.perform(post(String.format("/university/api/faculties/%d/students", 1))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .header("Accept", "application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    private Faculty createFaculty(Integer id, String name) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        return faculty;
    }

    private Student createStudent(Integer id, String name) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setBirthday(new Date());
        return student;
    }
}
