package com.pmt.iscod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.pmt.iscod.repositories.ProjectRepository;
import com.pmt.iscod.repositories.UserProjectRoleRepository;
import com.pmt.iscod.repositories.UserRepository;
import com.pmt.iscod.repositories.RoleRepository;
import com.pmt.iscod.services.ProjectService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock ProjectRepository projectRepository;
    @Mock UserRepository userRepository;
    @Mock UserProjectRoleRepository userProjectRoleRepository;
    @Mock RoleRepository roleRepository;
    @InjectMocks ProjectService projectService;

    @Test
    void createProject_ShouldCreateProjectAndAssignAdminRoleToUser() {
        User user = new User();
        user.setId(1L);
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Project project = new Project();
        project.setName("Test Project");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(java.util.Optional.of(adminRole));
        when(projectRepository.save(project)).thenReturn(project);

        Project createdProject = projectService.createProject(project, 1L);

        assertEquals("Test Project", createdProject.getName());
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects() {
        Project p1 = new Project();
        Project p2 = new Project();
        when(projectRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
    }

    @Test
    void requireRole_ShouldThrow_WhenUserHasNoRole() {
        when(userProjectRoleRepository.findByUserIdAndProjectId(1L, 1L)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> projectService.requireRole(1L, 1L, List.of("ADMIN")));
    }

    @Test
    void requireRole_ShouldThrow_WhenRoleNotInAllowedList() {
        UserProjectRole upr = new UserProjectRole();
        Role role = new Role();
        role.setName("OBSERVER");
        upr.setRole(role);
        when(userProjectRoleRepository.findByUserIdAndProjectId(1L, 1L)).thenReturn(upr);
        assertThrows(ResponseStatusException.class, () -> projectService.requireRole(1L, 1L, List.of("ADMIN", "MEMBER")));
    }

    @Test
    void requireRole_ShouldNotThrow_WhenRoleIsAllowed() {
        UserProjectRole upr = new UserProjectRole();
        Role role = new Role();
        role.setName("ADMIN");
        upr.setRole(role);
        when(userProjectRoleRepository.findByUserIdAndProjectId(1L, 1L)).thenReturn(upr);
        assertDoesNotThrow(() -> projectService.requireRole(1L, 1L, List.of("ADMIN", "MEMBER")));
    }

    @Test
    void getProjectMembers_ShouldReturnUsers() {
        User user = new User();
        user.setEmail("member@example.com");
        UserProjectRole upr = new UserProjectRole();
        upr.setUser(user);
        when(userProjectRoleRepository.findByProjectId(1L)).thenReturn(List.of(upr));
        List<User> result = projectService.getProjectMembers(1L);
        assertEquals(1, result.size());
        assertEquals("member@example.com", result.get(0).getEmail());
    }

    @Test
    void addUserToProjectByEmail_ShouldCreateUserProjectRole() {
        Project project = new Project();
        project.setId(1L);
        User user = new User();
        user.setEmail("member@example.com");
        Role role = new Role();
        role.setName("MEMBER");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(userRepository.findByEmail("member@example.com")).thenReturn(java.util.Optional.of(user));
        when(roleRepository.findByName("MEMBER")).thenReturn(java.util.Optional.of(role));

        projectService.addUserToProjectByEmail(1L, "member@example.com", "MEMBER");

        verify(userProjectRoleRepository).save(any(UserProjectRole.class));
    }
}
