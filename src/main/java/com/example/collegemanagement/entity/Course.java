package com.example.collegemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "courses_data")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseId;
    private String courseName;
    private int courseDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_information_id")
    private CourseInformation courseInformation;
}
