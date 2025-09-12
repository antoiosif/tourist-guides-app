import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-link-go-back-to',
  imports: [
    MatIconModule,
    RouterLink
  ],
  templateUrl: './link-go-back-to.component.html',
  styleUrl: './link-go-back-to.component.css'
})
export class LinkGoBackToComponent {
  @Input() text: string | undefined;
  @Input() link: string | undefined;
}
