package com.project.summoners_beta.model.dto;

import jakarta.validation.constraints.Size;

public class SummonCreationDTO {

    @Size(min = 3, max = 8)
    private String name;

    public SummonCreationDTO() {
    }

    public SummonCreationDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
