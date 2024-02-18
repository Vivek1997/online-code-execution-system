package com.vivek.onlinecodeexecutionsystem.dao.impl;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface LimitedRepository<T, ID> extends Repository<T, ID> {
    Iterable<T> findAll();

}
