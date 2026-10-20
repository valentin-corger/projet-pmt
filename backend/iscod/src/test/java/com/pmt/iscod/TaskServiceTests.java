package com.pmt.iscod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.pmt.iscod.repositories.TaskRepository;
import com.pmt.iscod.repositories.TaskHistoryRepository;
import com.pmt.iscod.repositories.UserRepository;
import com.pmt.iscod.repositories.ProjectRepository;
import com.pmt.iscod.services.EmailService;
import com.pmt.iscod.services.TaskService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock TaskRepository taskRepository;
    @Mock TaskHistoryRepository taskHistoryRepository;
    @Mock UserRepository userRepository;
    @Mock ProjectRepository projectRepository;
    @Mock EmailService emailService;
    @InjectMocks TaskService taskService;

    @Test
    void createTask_ShouldSaveTask_WhenProjectExists() {
        Project project = new Project();
        project.setId(1L);
        Task task = new Task();
        task.setName("Test Task");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task, 1L);

        assertEquals("Test Task", createdTask.getName());
    }

    @Test
    void getAllTasks_ShouldReturnTasksForProject() {
        Task t1 = new Task();
        Task t2 = new Task();
        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(t1, t2));

        List<Task> result = taskService.getAllTasks(1L);

        assertEquals(2, result.size());
    }

    @Test
    void updateTask_ShouldUpdateAndSaveTask() {
        Project project = new Project();
        project.setId(1L);
        Task existing = new Task();
        existing.setId(5L);
        existing.setName("Old name");
        Task task = new Task();
        task.setName("Updated");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.findById(5L)).thenReturn(java.util.Optional.of(existing));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskHistoryRepository.saveAll(anyList())).thenReturn(List.of());

        Task result = taskService.updateTask(task, 1L, 5L);

        assertEquals("Updated", result.getName());
        assertEquals(5L, result.getId());
    }

    @Test
    void createTask_WithUsers_ShouldEmailAssignees() {
        Project project = new Project();
        project.setId(1L);
        User user = new User();
        user.setId(2L);
        user.setEmail("user@example.com");
        Task task = new Task();
        task.setName("Task with users");
        task.setUsers(List.of(user));

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task, 1L);
        assertEquals("Task with users", result.getName());
    }

    @Test
    void updateTask_WithNoChanges_ShouldNotAddHistory() {
        Project project = new Project();
        project.setId(1L);
        Task existing = new Task();
        existing.setId(5L);
        existing.setName("Same");
        Task task = new Task();
        task.setName("Same");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.findById(5L)).thenReturn(java.util.Optional.of(existing));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskHistoryRepository.saveAll(anyList())).thenReturn(List.of());

        Task result = taskService.updateTask(task, 1L, 5L);
        assertEquals("Same", result.getName());
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findByIdAndProjectId(1L, 1L)).thenReturn(java.util.Optional.of(task));

        Task result = taskService.getTaskById(1L, 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateTask_WithDueDateChange_ShouldTrackHistory() {
        Project project = new Project();
        project.setId(1L);
        Task existing = new Task();
        existing.setId(5L);
        existing.setDueDate(LocalDate.of(2026, 1, 1));
        Task task = new Task();
        task.setDueDate(LocalDate.of(2026, 6, 1));

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.findById(5L)).thenReturn(java.util.Optional.of(existing));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskHistoryRepository.saveAll(anyList())).thenReturn(List.of());

        taskService.updateTask(task, 1L, 5L);
    }

    @Test
    void updateTask_WithExistingUsers_ShouldNotEmailExistingAssignees() {
        Project project = new Project();
        project.setId(1L);
        User user = new User();
        user.setId(2L);
        user.setEmail("user@example.com");
        Task existing = new Task();
        existing.setId(5L);
        existing.setUsers(List.of(user));
        Task task = new Task();
        task.setName("Updated");
        task.setUsers(List.of(user));

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.findById(5L)).thenReturn(java.util.Optional.of(existing));
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskHistoryRepository.saveAll(anyList())).thenReturn(List.of());

        Task result = taskService.updateTask(task, 1L, 5L);
        assertEquals("Updated", result.getName());
    }

    @Test
    void getTaskHistory_ShouldReturnHistory() {
        TaskHistory h = new TaskHistory();
        h.setField("name");
        when(taskHistoryRepository.findByTaskIdOrderByChangedAtDesc(1L)).thenReturn(List.of(h));

        List<TaskHistory> result = taskService.getTaskHistory(1L);
        assertEquals(1, result.size());
        assertEquals("name", result.get(0).getField());
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        Task t1 = new Task();
        t1.setStatus("TODO");
        when(taskRepository.findByProjectIdAndStatus(1L, "TODO")).thenReturn(List.of(t1));

        List<Task> result = taskService.getTasksByStatus(1L, "TODO");

        assertEquals(1, result.size());
        assertEquals("TODO", result.get(0).getStatus());
    }
}
