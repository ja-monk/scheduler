package com.github.ja_monk.scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {

    @Id     // specifies primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobs_seq_gen")
    @SequenceGenerator(name = "jobs_seq_gen", sequenceName = "jobs_seq", allocationSize = 1)
    private int id;
    private String name;
    private String definition;

    // constructor
    public Job(String name, int id, String definition) {
        this.name = name;
        this.id = id;
        this.definition = definition;
    }

    // default constructor, required by Hibernate, Jackson, etc.
    public Job() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition; 
    }


}
