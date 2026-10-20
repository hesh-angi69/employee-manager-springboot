package com.example.employee_manager.service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public List<Employee> findAll() { return repository.findAll(); }

    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    public Employee save(Employee employee) { return repository.save(employee); }

    public void delete(Long id) { repository.deleteById(id); }

    public boolean emailExists(String email) { return repository.existsByEmail(email); }

    public boolean emailExistsForOther(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }
}