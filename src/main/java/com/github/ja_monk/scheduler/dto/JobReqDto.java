package com.github.ja_monk.scheduler.dto;

public class JobReqDto {
    private String name;
    private String definition;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }


}
