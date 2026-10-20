import { Component } from '@angular/core';
import { signal } from '@angular/core';
import { Router } from '@angular/router';
import type { User } from '../../services/auth';
import { Auth } from '../../services/auth';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  isErrorMessage = signal(false);
  errorMessage = signal('');

  user: User = {
    username: '',
    email: '',
    password: '',
  }

  constructor(private auth: Auth, private router: Router,) { }

  onSubmit() {

    this.auth.register(this.user).subscribe({
      next: () => {
        this.router.navigate(['/login']);

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
