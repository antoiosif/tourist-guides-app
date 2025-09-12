import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { ViewportScroller } from '@angular/common';
import { VisitorsTableComponent } from './visitors-table/visitors-table.component';
import { UserService } from 'src/app/shared/services/user.service';
import { VisitorService } from 'src/app/shared/services/visitor.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { VisitorReadOnlyDTO } from 'src/app/shared/interfaces/visitor';
import { VisitorFilters } from 'src/app/shared/interfaces/filters';

@Component({
  selector: 'app-visitors-list',
  imports: [
    ReactiveFormsModule,
    VisitorsTableComponent
  ],
  templateUrl: './visitors-list.component.html',
  styleUrl: './visitors-list.component.css'
})
export class VisitorsListComponent implements OnInit {
  userService: UserService = inject(UserService);
  visitorService: VisitorService = inject(VisitorService);
  regionService: RegionService = inject(RegionService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);

  user = this.userService.user$;
  regions = this.regionService.regionsSorted$;
  visitors: VisitorReadOnlyDTO[] = [];
  filters: VisitorFilters | null = null;

  // for Pagination
  currentPage: number = 0;
  totalPages: number = 0;
  
  // Filtering Form
  form: FormGroup = new FormGroup({
    isActive: new FormControl(true),  // starts with showing only the active visitors
    uuid: new FormControl(null),
    userLastname: new FormControl(null),
    regionId: new FormControl('')
  });
  
  ngOnInit(): void {
    this.setFilters();
    this.getVisitors(this.filters);
  }

  onSubmit(): void {
    this.setFilters();
    this.getVisitors(this.filters);
  }

  onReset(): void {
    // Clean the form and set the initial values
    this.form.reset();
    this.form.get('isActive')?.setValue(true);
    this.form.get('regionId')?.setValue('');

    this.setFilters();
    this.getVisitors(this.filters);
  }

  setFilters(): void {
    this.filters = {
      page: null,           // default by backend is 0
      pageSize: null,       // default by backend is 10
      sortBy: null,         // default by backend is 'id'
      sortDirection: null,  // default by backend is 'ASC'
      isActive: this.form.get('isActive')?.value || null,
      uuid: this.form.get('uuid')?.value || null,
      userUsername: this.form.get('userUsername')?.value || null,
      userFirstname: null,
      userLastname: this.form.get('userLastname')?.value || null,
      regionId: this.form.get('regionId')?.value || null
    };
  }

  getVisitors(filters: VisitorFilters | null): void {
    this.visitorService.getVisitorsPaginatedFilteredSorted(filters)
      .subscribe((response) => {
        this.visitors = response.data;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
      })
  }

  // Pagination
  getNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      if (this.filters) this.filters.page = this.currentPage + 1;
      this.getVisitors(this.filters);
      this.scrollToTop();
    }
  }

  getPreviousPage(): void {
    if (this.currentPage > 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage - 1;
      this.getVisitors(this.filters);
      this.scrollToTop();
    }
  }

  selectPage(page: string): void {
    this.currentPage = parseInt(page) - 1;
    if (this.currentPage >= 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage;
      this.getVisitors(this.filters);
      this.scrollToTop();
    }  
  }
  
  formatInput(input: HTMLInputElement): void {
    input.value = input.value.replace(/[^0-9]/g, ''); // consumes the non-digit input characters
  }

  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
  }
}
