package com.github.ja_monk.scheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.ja_monk.scheduler.model.Job;

@Repository
public interface JobsRepository extends CrudRepository<Job, Integer> {

    
}
