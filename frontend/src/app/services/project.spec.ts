import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Project, ProjectData } from './project';

describe('Project', () => {
  let service: Project;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(Project);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getAllProjects() doit faire un GET sur /projects', () => {
    const projects: ProjectData[] = [{ id: 1, name: 'Projet A', description: 'desc', startDate: '2026-01-01' }];
    service.getAllProjects().subscribe(res => expect(res).toEqual(projects));
    const req = httpMock.expectOne('http://localhost:8080/projects');
    expect(req.request.method).toBe('GET');
    req.flush(projects);
  });

  it('createProject() doit faire un POST avec le userId en query param', () => {
    const project: ProjectData = { name: 'Nouveau projet', description: 'desc', startDate: '2026-01-01' };
    service.createProject(project, 1).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/projects?userId=1');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 1, ...project });
  });

  it('addUserToProject() doit faire un POST pour ajouter un membre', () => {
    service.addUserToProject(1, 'membre@test.com', 'MEMBER').subscribe();
    const req = httpMock.expectOne('http://localhost:8080/projects/1/users?userId=');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'membre@test.com', roleName: 'MEMBER' });
    req.flush({});
  });
});
