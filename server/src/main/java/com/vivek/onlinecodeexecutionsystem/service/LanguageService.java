package com.vivek.onlinecodeexecutionsystem.service;

import com.vivek.onlinecodeexecutionsystem.dto.LanguageDTO;
import com.vivek.onlinecodeexecutionsystem.model.Language;

import java.util.List;


public interface LanguageService {
    List<Language> getAllLanguages();

    Language getLanguage(int languageId);

    LanguageDTO convertLanguageEntityToDTO(Language language);

    Language convertToLanguageEntity(LanguageDTO languageDTO);
}
