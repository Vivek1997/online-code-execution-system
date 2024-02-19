package com.vivek.onlinecodeexecutionsystem.service;

import com.vivek.onlinecodeexecutionsystem.model.Language;

import java.util.List;
import java.util.Optional;


public interface LanguageService {
    List<Language> getAllLanguages();

    Optional<Language> getLanguage(long languageId);
}
