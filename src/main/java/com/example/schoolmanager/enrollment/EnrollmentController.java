package com.example.schoolmanager.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public EnrollmentDTO enroll(@RequestBody EnrollmentDTO dto) {
        return enrollmentService.enrollStudent(dto);
    }

    @GetMapping
    public List<EnrollmentDTO> getAll() {
        return enrollmentService.getAll();
    }

    @GetMapping("/student/{id}")
    public List<EnrollmentDTO> getByStudent(@PathVariable Long id) {
        return enrollmentService.getByStudent(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        enrollmentService.delete(id);
    }
}