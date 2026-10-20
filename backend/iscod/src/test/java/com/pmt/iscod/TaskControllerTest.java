package com.pmt.iscod;

import com.pmt.iscod.controllers.TaskController;
import com.pmt.iscod.services.TaskService;
import com.pmt.iscod.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void getAllTasks_ShouldReturn200() throws Exception {
        when(taskService.getAllTasks(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void createTask_ShouldReturn200AndReturnTask() throws Exception {
        Task task = new Task();
        task.setName("Test Task");
        when(taskService.createTask(any(), anyLong())).thenReturn(task);

        mockMvc.perform(post("/projects/1/tasks")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Task\",\"status\":\"TODO\",\"priority\":\"MEDIUM\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask_ShouldReturn200AndReturnUpdatedTask() throws Exception {
        Task task = new Task();
        task.setName("Updated Task");
        when(taskService.updateTask(any(), anyLong(), anyLong())).thenReturn(task);

        mockMvc.perform(put("/projects/1/tasks/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\",\"status\":\"DONE\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getTaskById_ShouldReturn200() throws Exception {
        Task task = new Task();
        task.setName("Test Task");
        when(taskService.getTaskById(1L, 1L)).thenReturn(task);

        mockMvc.perform(get("/projects/1/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getTasksByStatus_ShouldReturn200() throws Exception {
        when(taskService.getTasksByStatus(1L, "TODO")).thenReturn(List.of());

        mockMvc.perform(get("/projects/1/tasks/status/TODO"))
                .andExpect(status().isOk());
    }
}
