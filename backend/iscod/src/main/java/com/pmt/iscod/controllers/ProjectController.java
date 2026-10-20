package com.pmt.iscod.controllers;

import com.pmt.iscod.services.ProjectService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.pmt.iscod.Project;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
class MemberRequest {
    private String email;
    private String roleName;
}

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:60730" })
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public Project createProject(@RequestBody Project project, @RequestParam Long userId) {
        return projectService.createProject(project, userId);
    }

    @PostMapping("/{projectId}/users")
    public void addUserToProject(@PathVariable Long projectId, @RequestParam Long userId, @RequestBody MemberRequest request) {
        projectService.requireRole(userId, projectId, List.of("ADMIN"));
        projectService.addUserToProjectByEmail(projectId, request.getEmail(), request.getRoleName());
    }

    @GetMapping("/{projectId}/users")
    public List getProjectMembers(@PathVariable Long projectId) {
        return projectService.getProjectMembers(projectId);
    }

}
