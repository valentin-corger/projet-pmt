import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TaskUser } from './task';

export type ProjectData = {
  id?: number;
  name: string;
  description: string;
  startDate: string;
}


@Injectable({
  providedIn: 'root',
})
export class Project {
  private apiUrl = "http://localhost:8080/projects";

  constructor(private http: HttpClient) { }

  getAllProjects() {
    return this.http.get<ProjectData[]>(`${this.apiUrl}`);
  }

  createProject(project: ProjectData, userId: number) {
    return this.http.post<ProjectData>(`${this.apiUrl}?userId=${userId}`, project);
  }

  private get currentUserId(): string {
    return localStorage.getItem('userId') ?? '';
  }

  addUserToProject(projectId: number, email: string, roleName: string) {
    return this.http.post(`${this.apiUrl}/${projectId}/users?userId=${this.currentUserId}`, { email, roleName });
  }

  getProjectMembers(projectId: number) {
    return this.http.get<TaskUser[]>(`${this.apiUrl}/${projectId}/users`);
  }



}
