package com.vivek.onlinecodeexecutionsystem.service.impl;

import com.vivek.onlinecodeexecutionsystem.dao.LanguageDao;
import com.vivek.onlinecodeexecutionsystem.dto.LanguageDTO;
import com.vivek.onlinecodeexecutionsystem.exceptions.NoSuchLanguageException;
import com.vivek.onlinecodeexecutionsystem.model.Language;
import com.vivek.onlinecodeexecutionsystem.service.LanguageService;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {
    
    private final Logger logger = LoggerFactory.getLogger(LanguageServiceImpl.class);
    private final LanguageDao languageDao;
    private final ModelMapper modelMapper;

    @Autowired
    public LanguageServiceImpl(LanguageDao languageDao, ModelMapper modelMapper) {
        this.languageDao = languageDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Language> getAllLanguages() {
        Iterable<Language> languages = languageDao.findAll();
        return IterableUtils.toList(languages);
    }

    @Override
    public Language getLanguage(int languageId) throws NoSuchLanguageException {
        return languageDao.findById((long) languageId).orElseThrow(() -> new NoSuchLanguageException("Language does not exist with id:" + languageId));
    }

    @Override
    public LanguageDTO convertLanguageEntityToDTO(Language language) {
        return modelMapper.map(language, LanguageDTO.class);
    }

    @Override
    public Language convertToLanguageEntity(LanguageDTO languageDTO) {
        return modelMapper.map(languageDTO, Language.class);
    }
}
