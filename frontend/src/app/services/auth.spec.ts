import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Auth } from './auth';

describe('Auth', () => {
  let service: Auth;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(Auth);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('register() doit faire un POST vers /users/register', () => {
    const user = { username: 'test', email: 'test@test.com', password: '1234' };
    service.register(user).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/users/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(user);
    req.flush({ id: 1, ...user });
  });

  it('login() doit faire un POST vers /users/login', () => {
    const user = { username: 'test', email: 'test@test.com', password: '1234' };
    service.login(user).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/users/login');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 1, ...user });
  });
});
