package com.little.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.little.project.entities.Course;

public interface CourseRepository extends JpaRepository<Course,Long>{
    Course findByName(String name);
}