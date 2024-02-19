package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.LanguageDao;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageDao languageDao;

    @Autowired
    public LanguageServiceImpl(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    @Override
    public List<Language> getAllLanguages() {
        Iterable<Language> languages = languageDao.findAll();
        return IterableUtils.toList(languages);
    }

    @Override
    public Optional<Language> getLanguage(long languageId) {
        return languageDao.findById(languageId);
    }
}
