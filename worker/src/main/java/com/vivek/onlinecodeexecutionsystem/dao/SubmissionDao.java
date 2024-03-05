package com.vivek.onlinecodeexecutionsystem.dao;

import com.vivek.onlinecodeexecutionsystem.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionDao extends JpaRepository<Submission, Long> {
}