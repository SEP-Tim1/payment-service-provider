import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-auth-form',
  templateUrl: './auth-form.component.html',
  styleUrls: ['./auth-form.component.css']
})
export class AuthFormComponent implements OnInit {

  constructor() { }

  @Output() userAuthAction = new EventEmitter<User>();
  @Input() title: string = '';
  @Input() action: string = '';
  
  user: User = new User('', '');

  ngOnInit(): void {
  }

  triggerAction() {
    this.userAuthAction.emit(this.user);
  }
}
