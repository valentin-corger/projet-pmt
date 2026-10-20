package com.pmt.iscod.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pmt.iscod.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


}
