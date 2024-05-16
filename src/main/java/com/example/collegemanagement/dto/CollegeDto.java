package com.example.collegemanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeDto {

    private String collegeName;
    private int collegeCode;
    private String address;
    private List<CourseInformationDto> courseInformationDtoList;
}
