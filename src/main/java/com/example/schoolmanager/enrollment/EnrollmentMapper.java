package com.example.schoolmanager.enrollment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    EnrollmentDTO toDTO(Enrollment enrollment);

    Enrollment toEntity(EnrollmentDTO dto);
}