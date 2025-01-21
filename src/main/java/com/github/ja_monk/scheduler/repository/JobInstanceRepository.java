package com.github.ja_monk.scheduler.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.ja_monk.scheduler.model.JobInstance;

@Repository
public interface JobInstanceRepository extends CrudRepository<JobInstance, Integer> {


    @Query(
        value = """
            select * from job_instances
            where status = 'WAITING'
            and scheduled_time = (
                select min(job_instances.scheduled_time) 
                from job_instances
                where status = 'WAITING')
            """, 
        nativeQuery = true
    )
    public ArrayList<JobInstance> findNextScheduledJobs();

}
