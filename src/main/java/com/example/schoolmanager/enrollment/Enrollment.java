package com.example.schoolmanager.enrollment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Enrollment {
    @Id
    @GeneratedValue
    private Long id;

    private Long studentId;
    private Long courseId;
}