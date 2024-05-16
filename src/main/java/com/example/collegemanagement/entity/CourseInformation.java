package com.example.collegemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(name = "coursesInf_data")
public class CourseInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    @OneToMany(mappedBy = "courseInformation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> coursesList;
}
