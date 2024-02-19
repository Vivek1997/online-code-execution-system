package com.vivek.onlinecodeexecutionsystem.dto;

import com.vivek.onlinecodeexecutionsystem.model.Language;
import org.modelmapper.ModelMapper;

public class LanguageDTO {
    private int id;
    private String name;

    public LanguageDTO() {
    }

    public LanguageDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language toEntity(ModelMapper modelMapper) {
        return modelMapper.map(this, Language.class);
    }

    public static LanguageDTO fromEntity(Language language, ModelMapper modelMapper) {
        return modelMapper.map(language, LanguageDTO.class);
    }
}
