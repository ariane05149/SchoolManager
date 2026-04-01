package com.example.schoolmanager.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    // Create a new course
    public CourseDTO createCourse(CourseDTO dto) {
        if (courseRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Course with this name already exists");
        }
        Course course = courseMapper.toEntity(dto);
        return courseMapper.toDTO(courseRepository.save(course));
    }

    // Get all courses
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get course by ID
    public CourseDTO getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Update course
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        return courseMapper.toDTO(courseRepository.save(course));
    }

    // Delete course
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}