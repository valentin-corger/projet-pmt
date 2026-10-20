import {Component, signal, WritableSignal} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Task, TaskData, TaskUser } from '../../services/task';
import { Project } from '../../services/project';
import {TaskModal} from './task-modal/task-modal';

@Component({
  selector: 'app-project-board',
  imports: [TaskModal, FormsModule],
  templateUrl: './project-board.html',
  styleUrl: './project-board.css',
})
export class ProjectBoard {

  tasks = signal<TaskData[]>([]);
  members = signal<TaskUser[]>([]);
  statuses: TaskData['status'][] = ['TODO', 'IN_PROGRESS', 'DONE'];
  projectId!: number;
  showTaskModal: WritableSignal<boolean> = signal(false);
  selectedTask: WritableSignal<TaskData | null> = signal(null);

  inviteEmail = '';
  inviteRole = 'MEMBER';
  inviteMessage = signal('');
  inviteError = signal(false);

  constructor(private task: Task, private project: Project, private route: ActivatedRoute) {}

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTasks();
    this.project.getProjectMembers(this.projectId).subscribe(m => this.members.set(m));
  }

  loadTasks() {
    this.task.getProjectTasks(this.projectId).subscribe((tasks) => {
      this.tasks.set(tasks);
    });
  }

  getTasksByStatus(status: TaskData['status']) {
    return this.tasks().filter(t => t.status === status);
  }

  openCreateModal() {
    this.selectedTask.set(null);
    this.showTaskModal.set(true);
  }

  openEditModal(task: TaskData) {
    this.selectedTask.set(task);
    this.showTaskModal.set(true);
  }

  closeModal() {
    this.showTaskModal.set(false);
    this.selectedTask.set(null);
  }

  inviteMember() {
    if (!this.inviteEmail) return;
    this.project.addUserToProject(this.projectId, this.inviteEmail, this.inviteRole).subscribe({
      next: () => {
        this.inviteError.set(false);
        this.inviteMessage.set('Membre invité avec succès.');
        this.inviteEmail = '';
        this.project.getProjectMembers(this.projectId).subscribe(m => this.members.set(m));
        setTimeout(() => this.inviteMessage.set(''), 3000);
      },
      error: (err) => {
        this.inviteError.set(true);
        this.inviteMessage.set(err.status === 403 ? 'Action non autorisée.' : 'Erreur lors de l\'invitation.');
        setTimeout(() => this.inviteMessage.set(''), 3000);
      }
    });
  }

}
