package com.github.ja_monk.scheduler.dto;

import com.github.ja_monk.scheduler.model.Job;

public class JobResDto {
    private int id;
    private String name;
    private String definition;

    public JobResDto (Job job) {
        this.id = job.getId();
        this.name = job.getName();
        this.definition = job.getDefinition();
    }

    // getters only since response data
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    
    public String getDefinition() {
        return this.definition;
    }
}
