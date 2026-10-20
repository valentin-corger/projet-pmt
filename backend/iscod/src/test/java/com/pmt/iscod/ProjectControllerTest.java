package com.pmt.iscod;

import com.pmt.iscod.controllers.ProjectController;
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

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void getAllProjects_ShouldReturn200() throws Exception {
        when(projectService.getAllProjects()).thenReturn(List.of());

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk());
    }

    @Test
    void createProject_ShouldReturn200AndReturnProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        when(projectService.createProject(any(), anyLong())).thenReturn(project);

        mockMvc.perform(post("/projects")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Project\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void getProjectMembers_ShouldReturn200() throws Exception {
        when(projectService.getProjectMembers(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1/users"))
                .andExpect(status().isOk());
    }

    @Test
    void addUserToProject_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/projects/1/users")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"roleName\":\"MEMBER\"}"))
                .andExpect(status().isOk());
    }
}
