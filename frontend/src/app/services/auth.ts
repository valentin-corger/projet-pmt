import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


export type User = {
  username: string;
  email: string;
  password: string;
};

@Injectable({
  providedIn: 'root',
})
export class Auth {

  private apiUrl = "http://localhost:8080/users";

  constructor(private http: HttpClient) {
  }

  register(user: User) {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(user: User) {
    return this.http.post(`${this.apiUrl}/login`, user);
  }
}