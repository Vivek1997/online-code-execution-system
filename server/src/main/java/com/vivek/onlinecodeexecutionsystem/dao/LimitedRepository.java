package com.vivek.onlinecodeexecutionsystem.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface LimitedRepository<T, ID> extends Repository<T, ID> {
    Iterable<T> findAll();

    Optional<T> findById(ID id);

}
