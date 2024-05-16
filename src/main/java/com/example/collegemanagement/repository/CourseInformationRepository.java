package com.example.collegemanagement.repository;

import com.example.collegemanagement.entity.CourseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInformationRepository extends JpaRepository<CourseInformation, Long> {
}
