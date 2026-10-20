package com.pmt.iscod.services;

import com.pmt.iscod.repositories.TaskRepository;
import com.pmt.iscod.repositories.TaskHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.pmt.iscod.Task;
import com.pmt.iscod.TaskHistory;
import com.pmt.iscod.User;
import com.pmt.iscod.Project;
import com.pmt.iscod.repositories.ProjectRepository;
import com.pmt.iscod.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final EmailService emailService;

    public List<Task> getAllTasks(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Transactional
    public Task createTask(Task task, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        task.setProject(project);
        if (task.getUsers() != null && !task.getUsers().isEmpty()) {
            List<User> managed = task.getUsers().stream()
                    .map(u -> userRepository.findById(u.getId()).orElseThrow())
                    .toList();
            task.setUsers(managed);
        }
        Task saved = taskRepository.save(task);
        if (saved.getUsers() != null) {
            saved.getUsers().forEach(u -> emailService.sendTaskAssigned(u.getEmail(), saved.getName()));
        }
        return saved;
    }

    @Transactional
    public Task updateTask(Task task, Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Task existing = taskRepository.findById(taskId).orElseThrow();

        List<TaskHistory> entries = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (!Objects.equals(existing.getName(), task.getName())) {
            entries.add(buildEntry(existing, "name", existing.getName(), task.getName(), now));
        }
        if (!Objects.equals(existing.getDescription(), task.getDescription())) {
            entries.add(buildEntry(existing, "description", existing.getDescription(), task.getDescription(), now));
        }
        if (!Objects.equals(existing.getStatus(), task.getStatus())) {
            entries.add(buildEntry(existing, "status", existing.getStatus(), task.getStatus(), now));
        }
        if (!Objects.equals(existing.getPriority(), task.getPriority())) {
            entries.add(buildEntry(existing, "priority", existing.getPriority(), task.getPriority(), now));
        }
        if (!Objects.equals(existing.getDueDate(), task.getDueDate())) {
            String oldDate = existing.getDueDate() != null ? existing.getDueDate().toString() : null;
            String newDate = task.getDueDate() != null ? task.getDueDate().toString() : null;
            entries.add(buildEntry(existing, "dueDate", oldDate, newDate, now));
        }

        task.setProject(project);
        task.setId(taskId);
        if (task.getUsers() != null && !task.getUsers().isEmpty()) {
            List<User> managed = task.getUsers().stream()
                    .map(u -> userRepository.findById(u.getId()).orElseThrow())
                    .toList();
            task.setUsers(managed);
        }
        List<Long> existingUserIds = existing.getUsers() != null
                ? existing.getUsers().stream().map(User::getId).toList()
                : List.of();

        Task saved = taskRepository.save(task);
        entries.forEach(e -> e.setTask(saved));
        taskHistoryRepository.saveAll(entries);

        if (saved.getUsers() != null) {
            saved.getUsers().stream()
                    .filter(u -> !existingUserIds.contains(u.getId()))
                    .forEach(u -> emailService.sendTaskAssigned(u.getEmail(), saved.getName()));
        }
        return saved;
    }

    private TaskHistory buildEntry(Task task, String field, String oldValue, String newValue, LocalDateTime changedAt) {
        TaskHistory entry = new TaskHistory();
        entry.setTask(task);
        entry.setField(field);
        entry.setOldValue(oldValue);
        entry.setNewValue(newValue);
        entry.setChangedAt(changedAt);
        return entry;
    }

    public Task getTaskById(Long projectId, Long taskId) {
        return taskRepository.findByIdAndProjectId(taskId, projectId).orElseThrow();
    }

    public List<TaskHistory> getTaskHistory(Long taskId) {
        return taskHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId);
    }

    public List<Task> getTasksByStatus(Long projectId, String status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }

}
