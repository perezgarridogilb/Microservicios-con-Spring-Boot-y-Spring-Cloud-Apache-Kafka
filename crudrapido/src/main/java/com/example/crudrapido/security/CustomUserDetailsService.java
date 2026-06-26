package com.example.crudrapido.security;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.Rol;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.model.User;
import com.example.crudrapido.repository.EmployeeRepository;
import com.example.crudrapido.repository.PatientRepository;
import com.example.crudrapido.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = userRepository.findByUser(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = user.getStudent();

        Rol rol;
        if (patientRepository.existsByStudent(student)) {
            rol = Rol.PACIENTE;            
        } else {
            Employee employee = employeeRepository.findByStudent(student)
            .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado"));

            rol = employee.getRol();
        }

        List<SimpleGrantedAuthority> authorityList = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + rol.name())
        );

        return new org.springframework.security.core.userdetails.User(
            user.getUser(),
            user.getPassword(),
            authorityList
        );
        }

}
