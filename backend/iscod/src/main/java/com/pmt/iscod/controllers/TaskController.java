package com.pmt.iscod.controllers;

import com.pmt.iscod.services.TaskService;
import com.pmt.iscod.services.ProjectService;
import com.pmt.iscod.TaskHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.pmt.iscod.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/projects/{projectId}")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:60730" })
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks(@PathVariable Long projectId) {
        return taskService.getAllTasks(projectId);
    }

    @PostMapping("/tasks")
    public Task createTask(@PathVariable Long projectId, @RequestParam Long userId, @RequestBody Task task) {
        projectService.requireRole(userId, projectId, List.of("ADMIN", "MEMBER"));
        return taskService.createTask(task, projectId);
    }

    @PutMapping("/tasks/{taskId}")
    public Task updateTask(@PathVariable Long projectId, @PathVariable Long taskId, @RequestParam Long userId, @RequestBody Task task) {
        projectService.requireRole(userId, projectId, List.of("ADMIN", "MEMBER"));
        return taskService.updateTask(task, projectId, taskId);
    }

    @GetMapping("/tasks/{taskId}")
    public Task getTaskById(@PathVariable Long projectId, @PathVariable Long taskId) {
        return taskService.getTaskById(projectId, taskId);
    }

    @GetMapping("/tasks/{taskId}/history")
    public List<TaskHistory> getTaskHistory(@PathVariable Long projectId, @PathVariable Long taskId) {
        return taskService.getTaskHistory(taskId);
    }

    @GetMapping("/tasks/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable Long projectId, @PathVariable String status) {
        return taskService.getTasksByStatus(projectId, status);
    }

}
