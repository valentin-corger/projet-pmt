import { Component, signal, WritableSignal } from '@angular/core';
import { Project, ProjectData } from '../../services/project';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  imports: [FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

  projects = signal<ProjectData[]>([]);
  showCreateModal: WritableSignal<boolean> = signal(false);

  newProject: ProjectData = { name: '', description: '', startDate: '' };

  constructor(private project: Project, private router: Router) {}

  ngOnInit() {
    this.project.getAllProjects().subscribe((projects) => {
      this.projects.set(projects);
    });
  }

  goToProject(id: number) {
    this.router.navigate(['/project', id]);
  }

  openCreateModal() {
    this.newProject = { name: '', description: '', startDate: '' };
    this.showCreateModal.set(true);
  }

  createProject() {
    const userId = Number(localStorage.getItem('userId'));
    this.project.createProject(this.newProject, userId).subscribe({
      next: () => {
        this.showCreateModal.set(false);
        this.project.getAllProjects().subscribe(p => this.projects.set(p));
      },
      error: (err) => console.error('Erreur création projet:', err)
    });
  }
}
