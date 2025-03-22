import {Component, inject} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AuthService} from './Service/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Frontend';
  authService: AuthService = inject(AuthService);
}
