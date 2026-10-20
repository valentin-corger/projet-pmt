import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Login } from './login';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([{ path: '**', redirectTo: '' }]),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  afterEach(() => { httpMock.verify(); });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit() doit faire un POST vers /users/login', () => {
    component.user = { username: 'test', email: 'test@test.com', password: '1234' };
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8080/users/login');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 42, username: 'test', email: 'test@test.com' });
  });

  it('onSubmit() en cas d\'erreur doit afficher le message d\'erreur', () => {
    component.onSubmit();
    const req = httpMock.expectOne('http://localhost:8080/users/login');
    req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    expect(component.isErrorMessage()).toBe(true);
  });
});
