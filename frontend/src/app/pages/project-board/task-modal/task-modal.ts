import {Component, Output, EventEmitter, input, OnInit, signal} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import {Task, TaskData, TaskHistoryEntry, TaskUser} from '../../../services/task';

@Component({
  selector: 'app-task-modal',
  imports: [FormsModule, DatePipe],
  templateUrl: './task-modal.html',
  styleUrl: './task-modal.css',
})
export class TaskModal implements OnInit {

  @Output() closed = new EventEmitter<void>();
  @Output() taskCreated = new EventEmitter<void>();

  readonly projectId = input.required<number>();
  readonly existingTask = input<TaskData | null>(null);
  readonly members = input<TaskUser[]>([]);

  history = signal<TaskHistoryEntry[]>([]);

  task: TaskData = {
    name: '',
    description: '',
    dueDate: '',
    priority: 'MEDIUM',
    status: 'TODO',
    users: []
  };

  ngOnInit() {
    const existing = this.existingTask();
    if (existing) {
      this.task = { ...existing };
      if (existing.id) {
        this.taskService.getTaskHistory(this.projectId(), existing.id).subscribe(h => this.history.set(h));
      }
    }
  }

  isAssigned(userId: number): boolean {
    return (this.task.users ?? []).some(u => u.id === userId);
  }

  toggleAssignee(member: TaskUser) {
    const current = this.task.users ?? [];
    if (this.isAssigned(member.id)) {
      this.task.users = current.filter(u => u.id !== member.id);
    } else {
      this.task.users = [...current, member];
    }
  }

  close() {
    this.closed.emit();
  }

  onSubmit() {
    const existingId = this.existingTask()?.id;
    const request = existingId
      ? this.taskService.updateTaskInProject(this.projectId(), existingId, this.task)
      : this.taskService.createTaskInProject(this.projectId(), this.task);

    request.subscribe({
      next: () => { this.taskCreated.emit(); this.close(); },
      error: (err) => console.error('erreur tache:', err)
    });
  }

  constructor(private taskService: Task) {}
}
