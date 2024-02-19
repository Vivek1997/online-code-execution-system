package com.vivek.onlinecodeexecutionsystem.controller;

import com.vivek.onlinecodeexecutionsystem.dto.LanguageDTO;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

//TODO: use controller adivce for exception handling
@RestController
@RequestMapping("languages")
public class LanguageController {

    private final LanguageService languageService;

    private final ModelMapper modelMapper;

    @Autowired
    public LanguageController(LanguageService languageService, ModelMapper modelMapper) {
        this.languageService = languageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<LanguageDTO>> getLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        List<LanguageDTO> languageDTOS = languages.stream().map(language -> LanguageDTO.fromEntity(language, modelMapper)).collect(Collectors.toList());
        return ResponseEntity.ok(languageDTOS);
    }
}
