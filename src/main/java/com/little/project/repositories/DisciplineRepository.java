package com.little.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.little.project.entities.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline,Long>{     
    Discipline findByName(String name);
}