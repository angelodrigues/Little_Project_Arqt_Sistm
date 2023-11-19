package com.little.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.little.project.entities.Student;

public interface StudentRepository extends JpaRepository<Student,Long>{    
}