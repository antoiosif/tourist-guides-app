import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Location } from '@angular/common'

@Component({
  selector: 'app-link-go-back',
  imports: [MatIconModule],
  templateUrl: './link-go-back.component.html',
  styleUrl: './link-go-back.component.css'
})
export class LinkGoBackComponent {
  location: Location = inject(Location);

  goBack(): void {
    this.location.back();
  }
}
