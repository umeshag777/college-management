package com.example.collegemanagement.service;

import com.example.collegemanagement.dto.CollegeDto;
import com.example.collegemanagement.dto.CollegeInformationDto;
import com.example.collegemanagement.dto.CourseDto;
import com.example.collegemanagement.dto.CourseInformationDto;
import com.example.collegemanagement.entity.College;
import com.example.collegemanagement.entity.Course;
import com.example.collegemanagement.entity.CourseInformation;
import com.example.collegemanagement.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollegeService {

    @Autowired
    private final CollegeRepository collegeRepository;

    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @Transactional
    public void addCollegeInformation(CollegeInformationDto collegeInformationDTO) {
        try {
            List<College> colleges = new ArrayList<>();

            for (CollegeDto collegeDto : collegeInformationDTO.getColleges()) {
                College college = new College();
                college.setCollegeName(collegeDto.getCollegeName());
                college.setCollegeCode(collegeDto.getCollegeCode());
                college.setAddress(collegeDto.getAddress());

                List<CourseInformation> courseInformations = new ArrayList<>();

                for (CourseInformationDto courseInformationDto : collegeDto.getCourseInformationDtoList()) {
                    CourseInformation courseInformation = getCourseInformation(courseInformationDto, college);
                    courseInformations.add(courseInformation);
                }

                college.setCoursesInformationList(courseInformations);
                colleges.add(college);
            }

            collegeRepository.saveAll(colleges);
        } catch (Exception e) {
           throw new RuntimeException("Error : Failed to add college information due to ", e);
        }
    }

    private static CourseInformation getCourseInformation(CourseInformationDto courseInformationDto, College college) {
        CourseInformation courseInformation = new CourseInformation();
        courseInformation.setCourseName(courseInformationDto.getCourseName());
        courseInformation.setCollege(college);

        List<Course> courses = new ArrayList<>();

        for (CourseDto courseDto : courseInformationDto.getCourseDtoList()) {
            Course course = new Course();
            course.setCourseId(courseDto.getCourseId());
            course.setCourseName(courseDto.getCourseName());
            course.setCourseDuration(courseDto.getCourseDuration());
            course.setCourseInformation(courseInformation);
            courses.add(course);
        }

        courseInformation.setCoursesList(courses);
        return courseInformation;
    }


    public CollegeDto getCollegeById(Long id) {
        try{
            Optional<College> collegeOptional = collegeRepository.findById(id);
            if(collegeOptional.isPresent()){
                College college = collegeOptional.get();
                return convertToDto(college);
            }
            else {
                return null;
            }
        } catch (Exception e){
            throw new RuntimeException("Error : An error occurred while fetching the college by id ",e);
        }
    }

    private CollegeDto convertToDto(College college) {
        CollegeDto collegeDto = new CollegeDto();
        collegeDto.setCollegeName(college.getCollegeName());
        collegeDto.setCollegeCode(college.getCollegeCode());
        collegeDto.setAddress(college.getAddress());

        List<CourseInformationDto> courseInformationDtoList = college.getCoursesInformationList().stream()
                .map(this::convertCourseInformationDto)
                .collect(Collectors.toList());
        collegeDto.setCourseInformationDtoList(courseInformationDtoList);
        
        return collegeDto;
    }

    private CourseInformationDto convertCourseInformationDto(CourseInformation courseInformation) {
        CourseInformationDto informationDto = new CourseInformationDto();
        informationDto.setCourseName(courseInformation.getCourseName());

        List<CourseDto> courseDtoList = courseInformation.getCoursesList().stream()
                .map(this::convertCourseDto)
                .collect(Collectors.toList());
        informationDto.setCourseDtoList(courseDtoList);
        return informationDto;
    }

    private CourseDto convertCourseDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(course.getCourseId());
        courseDto.setCourseName(course.getCourseName());
        courseDto.setCourseDuration(course.getCourseDuration());
        return courseDto;
    }

    @Transactional
    public CollegeDto updateCollege(Long id, CollegeDto collegeDTO) {
        try{
            Optional<College> collegeOptional = collegeRepository.findById(id);
            if(collegeOptional.isPresent()){
                College college = collegeOptional.get();
                updateCollegeData(college, collegeDTO);
                collegeRepository.save(college);
            }
            else {
                throw new RuntimeException("College not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error : An error occurred while updating the college details ",e);
        }
        return collegeDTO;
    }

    private void updateCollegeData(College college, CollegeDto collegeDto) {
        college.setCollegeName(collegeDto.getCollegeName());
        college.setCollegeCode(collegeDto.getCollegeCode());
        college.setAddress(collegeDto.getAddress());

        List<CourseInformation> updatedCourseInformations = new ArrayList<>();
        for (CourseInformationDto courseInformationDto : collegeDto.getCourseInformationDtoList()) {
            CourseInformation existingCourseInformation = findCourseInformationByName(college, courseInformationDto.getCourseName());
            if (existingCourseInformation != null) {
                updateCourseInformation(existingCourseInformation, courseInformationDto);
                updatedCourseInformations.add(existingCourseInformation);
            } else {
                updatedCourseInformations.add(convertCourseInformationEntity(courseInformationDto));
            }
        }
        college.setCoursesInformationList(updatedCourseInformations);
    }

    private CourseInformation findCourseInformationByName(College college, String courseName) {
        return college.getCoursesInformationList().stream()
                .filter(courseInformation -> courseInformation.getCourseName().equals(courseName))
                .findFirst()
                .orElse(null);
    }

    private void updateCourseInformation(CourseInformation courseInformation, CourseInformationDto courseInformationDto) {
        courseInformation.setCourseName(courseInformationDto.getCourseName());

        List<Course> updatedCourseList = new ArrayList<>();
        for (CourseDto courseDto : courseInformationDto.getCourseDtoList()) {
            Course existingCourse = findCourseByName(courseInformation, courseDto.getCourseName());
            if (existingCourse != null) {
                updateCourse(existingCourse, courseDto);
                updatedCourseList.add(existingCourse);
            } else {
                updatedCourseList.add(convertCourseEntity(courseDto));
            }
        }
        courseInformation.setCoursesList(updatedCourseList);
    }

    private Course findCourseByName(CourseInformation courseInformation, String courseName) {
        return courseInformation.getCoursesList().stream()
                .filter(course -> course.getCourseName().equals(courseName))
                .findFirst()
                .orElse(null);
    }

    private void updateCourse(Course course, CourseDto courseDto) {
        course.setCourseDuration(courseDto.getCourseDuration());
    }

    private CourseInformation convertCourseInformationEntity(CourseInformationDto courseInformationDto) {
        CourseInformation courseInformation = new CourseInformation();
        courseInformation.setCourseName(courseInformationDto.getCourseName());

        List<Course> courseList = new ArrayList<>();
        for (CourseDto courseDto : courseInformationDto.getCourseDtoList()) {
            courseList.add(convertCourseEntity(courseDto));
        }
        courseInformation.setCoursesList(courseList);
        return courseInformation;
    }

    private Course convertCourseEntity(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setCourseDuration(courseDto.getCourseDuration());
        return course;
    }


    @Transactional
    public void deleteCollege(Long id) {
        try {
            Optional<College> collegeOptional = collegeRepository.findById(id);
            if(collegeOptional.isPresent()){
                College college = collegeOptional.get();
                collegeRepository.delete(college);
            } else {
                throw new RuntimeException("College not found with id: " +id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error : An error occurred while deleting the college Information",e);
        }
    }

    @Transactional(readOnly = true)
    public List<CollegeDto> getAllColleges() {
        try {
            List<College> colleges = collegeRepository.findAll();
            return colleges.stream()
                    .map(this::convertToCollegeDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error: An error occurred while fetching all colleges", e);
        }
    }

    private CollegeDto convertToCollegeDto(College college) {
        CollegeDto collegeDto = new CollegeDto();
        collegeDto.setCollegeName(college.getCollegeName());
        collegeDto.setCollegeCode(college.getCollegeCode());
        collegeDto.setAddress(college.getAddress());

        List<CourseInformationDto> courseInformationDtoList = college.getCoursesInformationList().stream()
                .map(this::convertCourseInformationDto)
                .collect(Collectors.toList());
        collegeDto.setCourseInformationDtoList(courseInformationDtoList);
        return collegeDto;
    }

}
