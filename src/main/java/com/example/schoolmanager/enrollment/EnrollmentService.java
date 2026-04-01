package com.example.schoolmanager.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    // Create new enrollment
    public EnrollmentDTO enrollStudent(EnrollmentDTO dto) {

        if (enrollmentRepository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(dto.getStudentId());
        enrollment.setCourseId(dto.getCourseId());

        return enrollmentMapper.toDTO(enrollmentRepository.save(enrollment));
    }

    // Get all enrollments
    public List<EnrollmentDTO> getAll() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get by student
    public List<EnrollmentDTO> getByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Delete enrollment
    public void delete(Long id) {
        enrollmentRepository.deleteById(id);
    }
}