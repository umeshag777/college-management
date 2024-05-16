package com.example.collegemanagement.dto;

import lombok.Data;

import java.util.List;

@Data
public class CollegeInformationDto {

    private List<CollegeDto> colleges;
    private List<CourseInformationDto> courseInformationDtoList;
}
