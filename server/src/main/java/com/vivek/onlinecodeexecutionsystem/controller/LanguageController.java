package com.vivek.onlinecodeexecutionsystem.controller;

import com.vivek.onlinecodeexecutionsystem.dto.LanguageDTO;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

//TODO: use controller adivce for exception handling
@RestController
@RequestMapping("languages")
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LanguageDTO>> getLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        List<LanguageDTO> languageDTOS = languages.stream().map(languageService::convertLanguageEntityToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(languageDTOS);
    }

    @GetMapping(value = "{languageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LanguageDTO> getLanguage(@PathVariable int languageId) {
        Language language = languageService.getLanguage(languageId);
        LanguageDTO languageDTO = languageService.convertLanguageEntityToDTO(language);
        return ResponseEntity.ok(languageDTO);
    }
}
