import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Dashboard } from './dashboard';

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/projects').flush([]);
    await fixture.whenStable();
  });

  afterEach(() => { httpMock.verify(); });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('openCreateModal() doit afficher la modale', () => {
    component.openCreateModal();
    expect(component.showCreateModal()).toBe(true);
  });
});
