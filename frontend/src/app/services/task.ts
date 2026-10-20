import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export type TaskUser = { id: number; username: string; email: string };

export type TaskHistoryEntry = {
  id: number;
  field: string;
  oldValue: string | null;
  newValue: string | null;
  changedAt: string;
};

export type TaskData = {
  id?: number;
  name: string;
  description: string;
  dueDate: string;
  priority: string;
  status: string;
  users?: TaskUser[];
}

@Injectable({
  providedIn: 'root',
})
export class Task {
  private apiUrl = "http://localhost:8080/projects";

  constructor(private http: HttpClient) { }


  getProjectTasks(projectId: number) {
    return this.http.get<TaskData[]>(`${this.apiUrl}/${projectId}/tasks`);
  }

  private get currentUserId(): string {
    return localStorage.getItem('userId') ?? '';
  }

  createTaskInProject(projectId: number, task: TaskData) {
    return this.http.post<TaskData>(`${this.apiUrl}/${projectId}/tasks?userId=${this.currentUserId}`, task);
  }

  updateTaskInProject(projectId: number, taskId: number, task: TaskData) {
    return this.http.put<TaskData>(`${this.apiUrl}/${projectId}/tasks/${taskId}?userId=${this.currentUserId}`, task);
  }


  getTaskHistory(projectId: number, taskId: number) {
    return this.http.get<TaskHistoryEntry[]>(`${this.apiUrl}/${projectId}/tasks/${taskId}/history`);
  }

  getTasksByStatus(projectId: number, status: string) {
    return this.http.get<TaskData[]>(`${this.apiUrl}/${projectId}/tasks/status/${status}`);
  }



}
