import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { formatDate, ViewportScroller } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UserService } from 'src/app/shared/services/user.service';
import { ActivityService } from 'src/app/shared/services/activity.service';
import { CategoryService } from 'src/app/shared/services/category.service';
import { ActivityReadOnlyDTO } from 'src/app/shared/interfaces/activity';
import { ActivityFilters } from 'src/app/shared/interfaces/filters';

@Component({
  selector: 'app-activities-list',
  imports: [
    ReactiveFormsModule,
    RouterLink
],
  templateUrl: './activities-list.component.html',
  styleUrl: './activities-list.component.css'
})
export class ActivitiesListComponent implements OnInit {
  userService: UserService = inject(UserService);
  activityService: ActivityService = inject(ActivityService);
  categoryService: CategoryService = inject(CategoryService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  
  user = this.userService.user$;
  categories = this.categoryService.categoriesSorted$;
  activities: ActivityReadOnlyDTO[] = [];
  filters: ActivityFilters | null = null;

  // for Pagination
  currentPage: number = 0;
  totalPages: number = 0;

  // Filtering Form visible to role 'SUPER_ADMIN'
  form: FormGroup = new FormGroup({
    uuid: new FormControl(null),
    title: new FormControl(null),
    status: new FormControl(''),
    categoryId: new FormControl('')
  });
  
  ngOnInit(): void {
    if (this.user()?.role === 'SUPER_ADMIN') {
      this.setFilters(); 
    } else {
      this.setGuideFilters(null);
    }
    this.getActivities(this.filters);
  }

  // for Filtering Form visible to role 'SUPER_ADMIN'
  onSubmit(): void {
    this.setFilters();
    this.getActivities(this.filters);
  }

  onReset(): void {
    // Clean the form and set the initial values
    this.form.reset();
    this.form.get('status')?.setValue('');
    this.form.get('categoryId')?.setValue('');

    this.setFilters();
    this.getActivities(this.filters);
  }

  setFilters(): void {
    this.filters = {
      page: null,             // default by backend is 0
      pageSize: 6,
      sortBy: 'status',       // first 'ACTIVE'
      sortDirection: null,    // default by backend is 'ASC'
      uuid: this.form.get('uuid')?.value || null,
      title: this.form.get('title')?.value || null,
      status: this.form.get('status')?.value || null,
      categoryId: this.form.get('categoryId')?.value || null
    };
  }

  // for Filtering Buttons visible to role 'GUIDE' 
  onSubmitCategory(id: bigint | null): void {
    this.setGuideFilters(id);
    this.getActivities(this.filters);
  }

  setGuideFilters(id: bigint | null): void {
    this.filters = {
      page: null,             // default by backend is 0
      pageSize: 6,
      sortBy: 'dateTime',     // shows the more recent first
      sortDirection: null,    // default by backend is 'ASC'
      uuid: null,
      title: null,
      status: 'ACTIVE',       // shows only the 'ACTIVE' activities
      categoryId: id || null
    };
  }

  getActivities(filters: ActivityFilters | null): void {
    this.activityService.getActivitiesPaginatedFilteredSorted(filters)
      .subscribe((response) => {
        this.activities = response.data;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
      })
  }

  // Pagination
  getNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      if (this.filters) this.filters.page = this.currentPage + 1;
      this.getActivities(this.filters);
      this.scrollToTop();
    }
  }

  getPreviousPage(): void {
    if (this.currentPage > 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage - 1;
      this.getActivities(this.filters);
      this.scrollToTop();
    }
  }

  selectPage(page: string): void {
    this.currentPage = parseInt(page) - 1;
    if (this.currentPage >= 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage;
      this.getActivities(this.filters);
      this.scrollToTop();
    }  
	}
  
  formatInput(input: HTMLInputElement): void {
		input.value = input.value.replace(/[^0-9]/g, ''); // consumes the non-digit input characters
	}

  formatDate(dateTime: string): string {
    return formatDate(dateTime, 'dd-MM-yyyy', 'en');
  }

  scrollToTop(): void {
    this.viewportScroller.scrollToPosition([0, 0]);
  }
}
