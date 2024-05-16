package com.example.collegemanagement.controller;

import com.example.collegemanagement.dto.CollegeDto;
import com.example.collegemanagement.dto.CollegeInformationDto;
import com.example.collegemanagement.dto.CourseDto;
import com.example.collegemanagement.dto.CourseInformationDto;
import com.example.collegemanagement.service.CollegeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollegeController.class)
public class CollegeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollegeService collegeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCollege() throws Exception {
        CollegeInformationDto collegeInformationDto = getCollegeInformationDto();
        doNothing().when(collegeService).addCollegeInformation(collegeInformationDto);

        mockMvc.perform(post("/api/colleges/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(collegeInformationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCollegeById() throws Exception {
        CollegeDto collegeDto = new CollegeDto();
        when(collegeService.getCollegeById(anyLong())).thenReturn(collegeDto);

        mockMvc.perform(get("/api/colleges/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateCollege() throws Exception {
        CollegeDto collegeDto = getCollegeDto();
        when(collegeService.updateCollege(anyLong(), any(CollegeDto.class))).thenReturn(collegeDto);

        mockMvc.perform(put("/api/colleges/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(collegeDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("College information updated successfully."));
    }

    @Test
    public void testDeleteCollege() throws Exception {
        doNothing().when(collegeService).deleteCollege(anyLong());

        mockMvc.perform(delete("/api/colleges/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("College information deleted successfully."));
    }

    @Test
    public void testGetAllColleges() throws Exception {
        when(collegeService.getAllColleges()).thenReturn(getListOfColleges());

        mockMvc.perform(get("/api/colleges"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private CollegeDto getCollegeDto() {
        CollegeDto collegeDto = new CollegeDto();
        collegeDto.setCollegeName("ANC");
        collegeDto.setAddress("NDL");
        collegeDto.setCollegeCode(123);
        collegeDto.setCourseInformationDtoList(getListOfCourseInformationDto());
        return collegeDto;
    }

    private CollegeInformationDto getCollegeInformationDto() {
        CollegeInformationDto collegeInformationDto = new CollegeInformationDto();
        collegeInformationDto.setColleges(getListOfColleges());
        collegeInformationDto.setCourseInformationDtoList(getListOfCourseInformationDto());

        return collegeInformationDto;
    }

    private List<CourseInformationDto> getListOfCourseInformationDto() {
        List<CourseInformationDto> courseInformationDtoList = new ArrayList<>();

        CourseInformationDto informationDto1 = new CourseInformationDto();
        informationDto1.setCourseName("PCB");
        informationDto1.setCourseDtoList(getCourseDtoList());

        courseInformationDtoList.add(informationDto1);
        return courseInformationDtoList;
    }

    private List<CourseDto> getCourseDtoList() {
        List<CourseDto> courseDtoList = new ArrayList<>();

        CourseDto courseDto1 = new CourseDto();
        courseDto1.setCourseDuration(4);
        courseDto1.setCourseId("1");
        courseDto1.setCourseName("Maths");

        courseDtoList.add(courseDto1);
        return courseDtoList;
    }

    private List<CollegeDto> getListOfColleges() {
        List<CollegeDto> collegeDtoList = new ArrayList<>();

        CollegeDto collegeDto1 = new CollegeDto();
        collegeDto1.setCollegeCode(1);
        collegeDto1.setCollegeName("ABC");
        collegeDto1.setAddress("ABC");
        collegeDto1.setCourseInformationDtoList(getListOfCourseInformationDto());

        collegeDtoList.add(collegeDto1);
        return collegeDtoList;
    }
}
