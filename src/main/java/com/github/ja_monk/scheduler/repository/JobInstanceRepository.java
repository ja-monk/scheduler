package com.github.ja_monk.scheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.ja_monk.scheduler.model.JobInstance;

@Repository
public interface JobInstanceRepository extends CrudRepository<JobInstance, Integer> {}
