package com.example.schoolmanager.student;

import com.example.schoolmanager.auth.AuthResponse;
import com.example.schoolmanager.auth.JwtUtil;
import com.example.schoolmanager.user.AuthProvider;
import com.example.schoolmanager.user.Role;
import com.example.schoolmanager.user.User;
import com.example.schoolmanager.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Manage students")
public class StudentController {

    private final StudentService studentService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody StudentRegisterRequest request) {

        if (userService.getByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse("Email already exists. Please login instead.")
            );
        }

        User student = new User();
        student.setEmail(request.getEmail());
        student.setName(request.getFirstName() + " " + request.getLastName());
        student.setPassword(userService.getPasswordEncoder().encode(request.getPassword()));
        student.setRole(Role.STUDENT);
        student.setProvider(AuthProvider.LOCAL);

        userService.save(student);

        // Create JWT token immediately
        String token = jwtUtil.generateToken(student.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public List<StudentDTO> getAll() {
        return studentService.getAllStudents();
    }

    @PostMapping("/admin/create")
    @SecurityRequirement(name = "bearerAuth")
    public StudentDTO createStudent(@RequestBody StudentDTO dto) {
        return studentService.createStudent(dto);
    }

    @PutMapping("/admin/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public StudentDTO updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
        return studentService.updateStudent(id, dto);
    }

    @DeleteMapping("/admin/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}