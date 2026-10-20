import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Auth } from '../../services/auth';
import { Router } from '@angular/router';
import type { User } from '../../services/auth';
import { signal } from '@angular/core';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {



  isErrorMessage = signal(false);
  errorMessage = signal('');

  user: User = {
    username: '',
    email: '',
    password: '',
  }

  constructor(private auth: Auth, private router: Router,) { }

  onSubmit() {

    this.auth.login(this.user).subscribe({
      next: (user: any) => {
        localStorage.setItem('userId', user.id);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isErrorMessage.set(true);
        this.errorMessage.set('Identifiants incorrects');
        setTimeout(() => {
          this.isErrorMessage.set(false);
        }, 3000);
      }
    })

  }





}
