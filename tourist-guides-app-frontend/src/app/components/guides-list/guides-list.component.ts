import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { ViewportScroller } from '@angular/common';
import { GuidesTableComponent } from './guides-table/guides-table.component';
import { UserService } from 'src/app/shared/services/user.service';
import { GuideService } from 'src/app/shared/services/guide.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { LanguageService } from 'src/app/shared/services/language.service';
import { GuideReadOnlyDTO } from 'src/app/shared/interfaces/guide';
import { GuideFilters } from 'src/app/shared/interfaces/filters';

@Component({
  selector: 'app-guides-list',
  imports: [
    ReactiveFormsModule,
    GuidesTableComponent
  ],
  templateUrl: './guides-list.component.html',
  styleUrl: './guides-list.component.css'
})
export class GuidesListComponent implements OnInit {
  userService: UserService = inject(UserService);
  guideService: GuideService = inject(GuideService);
  regionService: RegionService = inject(RegionService);
  languageService: LanguageService = inject(LanguageService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);

  user = this.userService.user$;
  regions = this.regionService.regionsSorted$;
  languages = this.languageService.languagesSorted$;
  guides: GuideReadOnlyDTO[] = [];
  filters: GuideFilters | null = null;

  // for Pagination
  currentPage: number = 0;
  totalPages: number = 0;

  // Filtering Form
  form: FormGroup = new FormGroup({
    isActive: new FormControl(true),  // starts with showing only the active guides
    uuid: new FormControl(null),
    recordNumber: new FormControl(null),
    userLastname: new FormControl(null),
    regionId: new FormControl(''),
    languageId: new FormControl('')
  });

  ngOnInit(): void {
    this.setFilters();
    this.getGuides(this.filters); 
  }

  onSubmit(): void {
    this.setFilters();
    this.getGuides(this.filters);
  }

  onReset(): void {
    // Clean the form and set the initial values
    this.form.reset();
    this.form.get('isActive')?.setValue(true);
    this.form.get('regionId')?.setValue('');
    this.form.get('languageId')?.setValue('');

    this.setFilters();
    this.getGuides(this.filters);
  }

  setFilters(): void {
    this.filters = {
      page: null,           // default by backend is 0
      pageSize: null,       // default by backend is 10
      sortBy: null,         // default by backend is 'id'
      sortDirection: null,  // default by backend is 'ASC'
      isActive: this.form.get('isActive')?.value || null,
      uuid: this.form.get('uuid')?.value || null,
      recordNumber: this.form.get('recordNumber')?.value || null,
      userUsername: this.form.get('userUsername')?.value || null,
      userFirstname: null,
      userLastname: this.form.get('userLastname')?.value || null,
      regionId: this.form.get('regionId')?.value || null,
      languageId: this.form.get('languageId')?.value || null
    };
  }

  getGuides(filters: GuideFilters | null): void {
    this.guideService.getGuidesPaginatedFilteredSorted(filters)
      .subscribe((response) => {
        this.guides = response.data;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
      })
  }
  
  // Pagination
  getNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      if (this.filters) this.filters.page = this.currentPage + 1;
      this.getGuides(this.filters);
      this.scrollToTop();
    }
  }

  getPreviousPage(): void {
    if (this.currentPage > 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage - 1;
      this.getGuides(this.filters);
      this.scrollToTop();
    }
  }

  selectPage(page: string): void {
    this.currentPage = parseInt(page) - 1;
    if (this.currentPage >= 0 && this.currentPage < this.totalPages) {
      if (this.filters) this.filters.page = this.currentPage;
      this.getGuides(this.filters);
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
