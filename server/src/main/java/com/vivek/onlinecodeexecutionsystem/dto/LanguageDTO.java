package com.vivek.onlinecodeexecutionsystem.dto;

public class LanguageDTO {
    private int id;
    private String name;
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "LanguageDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
