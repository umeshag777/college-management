package com.example.collegemanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseInformationDto {

    private String courseName;
    private List<CourseDto> courseDtoList;
}
