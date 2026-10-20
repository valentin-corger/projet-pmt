package com.pmt.iscod.services;

import com.pmt.iscod.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import com.pmt.iscod.Project;
import com.pmt.iscod.repositories.UserRepository;

import jakarta.transaction.Transactional;

import com.pmt.iscod.repositories.RoleRepository;
import com.pmt.iscod.repositories.UserProjectRoleRepository;
import com.pmt.iscod.User;
import com.pmt.iscod.Role;
import com.pmt.iscod.UserProjectRole;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public Project createProject(Project project, Long userId ) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByName("ADMIN").orElseThrow();


        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setUser(user);
        Project savedProject = projectRepository.save(project);
        userProjectRole.setProject(savedProject);
        userProjectRole.setRole(role);
        userProjectRoleRepository.save(userProjectRole);
        return savedProject; 
    }

    public List<User> getProjectMembers(Long projectId) {
        return userProjectRoleRepository.findByProjectId(projectId)
                .stream()
                .map(UserProjectRole::getUser)
                .toList();
    }

    public void requireRole(Long userId, Long projectId, List<String> allowedRoles) {
        UserProjectRole upr = userProjectRoleRepository.findByUserIdAndProjectId(userId, projectId);
        if (upr == null || !allowedRoles.contains(upr.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Action non autorisée pour ce rôle");
        }
    }

    public void addUserToProjectByEmail(Long projectId, String email, String roleName) {
        // récupérer les entites concernee

        Project project = projectRepository.findById(projectId).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();
        Role role = roleRepository.findByName(roleName).orElseThrow();

        UserProjectRole userProjectRole = new UserProjectRole();

        userProjectRole.setProject(project);
        userProjectRole.setUser(user);
        userProjectRole.setRole(role);
        userProjectRoleRepository.save(userProjectRole);



    }

}
