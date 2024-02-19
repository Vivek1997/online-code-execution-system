package com.vivek.onlinecodeexecutionsystem.dao;

import com.vivek.onlinecodeexecutionsystem.model.Language;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageDao extends LimitedRepository<Language, Long> {
}
