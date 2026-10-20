package com.pmt.iscod.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pmt.iscod.UserProjectRole;

@Repository
public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole, Long> {

    UserProjectRole findByUserIdAndProjectId(Long userId, Long projectId);

    List<UserProjectRole> findByProjectId(Long projectId);
}
