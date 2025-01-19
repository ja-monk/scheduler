package com.github.ja_monk.scheduler.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.ja_monk.scheduler.model.Job;

@Repository
public interface JobsRepository extends CrudRepository<Job, Integer> {
    
    public Optional<Job> findByName(String name);
}
