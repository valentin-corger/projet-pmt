import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TaskModal } from './task-modal';

describe('TaskModal', () => {
  let component: TaskModal;
  let fixture: ComponentFixture<TaskModal>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [TaskModal],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(TaskModal);
    fixture.componentRef.setInput('projectId', 1);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  afterEach(() => { httpMock.verify(); });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('close() doit émettre l\'événement closed', () => {
    let emitted = false;
    component.closed.subscribe(() => emitted = true);
    component.close();
    expect(emitted).toBe(true);
  });

  it('onSubmit() sans tâche existante doit faire un POST pour créer une tâche', () => {
    let taskCreatedEmitted = false;
    component.taskCreated.subscribe(() => taskCreatedEmitted = true);
    component.task = { name: 'Test', description: 'desc', dueDate: '2026-01-01', priority: 'HIGH', status: 'TODO' };
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8080/projects/1/tasks?userId=');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 1, ...component.task });
    expect(taskCreatedEmitted).toBe(true);
  });
});
