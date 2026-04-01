package com.example.schoolmanager.student;

import com.example.schoolmanager.user.Role;
import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserService userService; // For creating student user

    // List all students
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    // Admin creates student
    public StudentDTO createStudent(StudentDTO dto) {
        Student student = studentMapper.toEntity(dto);
        return studentMapper.toDTO(studentRepository.save(student));
    }

    // Admin updates student
    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        return studentMapper.toDTO(studentRepository.save(student));
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public User registerStudent(StudentRegisterRequest request) {

        //  account
        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                Role.STUDENT
        );

        //profile of stdnt
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());

        studentRepository.save(student);

        return user;
    }
}