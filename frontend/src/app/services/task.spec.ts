import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Task, TaskData } from './task';

describe('Task', () => {
  let service: Task;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(Task);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getProjectTasks() doit faire un GET sur les taches du projet', () => {
    const tasks: TaskData[] = [{ id: 1, name: 'Task 1', description: 'desc', dueDate: '2026-01-01', priority: 'HIGH', status: 'TODO' }];
    service.getProjectTasks(1).subscribe(res => expect(res).toEqual(tasks));
    const req = httpMock.expectOne('http://localhost:8080/projects/1/tasks');
    expect(req.request.method).toBe('GET');
    req.flush(tasks);
  });

  it('createTaskInProject() doit faire un POST pour creer une tache', () => {
    const task: TaskData = { name: 'Nouvelle tache', description: 'desc', dueDate: '2026-01-01', priority: 'LOW', status: 'TODO' };
    service.createTaskInProject(1, task).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/projects/1/tasks?userId=');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(task);
    req.flush({ id: 1, ...task });
  });

  it('updateTaskInProject() doit faire un PUT pour modifier une tache', () => {
    const task: TaskData = { name: 'Modif', description: 'desc', dueDate: '2026-01-01', priority: 'HIGH', status: 'DONE' };
    service.updateTaskInProject(1, 5, task).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/projects/1/tasks/5?userId=');
    expect(req.request.method).toBe('PUT');
    req.flush({ id: 5, ...task });
  });

  it('getTasksByStatus() doit filtrer les taches par statut', () => {
    const tasks: TaskData[] = [{ id: 1, name: 'Task', description: '', dueDate: '2026-01-01', priority: 'LOW', status: 'TODO' }];
    service.getTasksByStatus(1, 'TODO').subscribe(res => expect(res.length).toBe(1));
    const req = httpMock.expectOne('http://localhost:8080/projects/1/tasks/status/TODO');
    expect(req.request.method).toBe('GET');
    req.flush(tasks);
  });
});
