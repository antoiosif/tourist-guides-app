import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { VisitorRegisterComponent } from './components/visitor-register/visitor-register.component';
import { LoginComponent } from './components/login/login.component';
import { GuidesListComponent } from './components/guides-list/guides-list.component';
import { RegionComponent } from './components/region/region.component';
import { LanguageComponent } from './components/language/language.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { GuideProfileComponent } from './components/guide-profile/guide-profile.component';
import { GuideAccountComponent } from './components/guide-account/guide-account.component';
import { CategoryComponent } from './components/category/category.component';
import { VisitorAccountComponent } from './components/visitor-account/visitor-account.component';
import { VisitorsListComponent } from './components/visitors-list/visitors-list.component';
import { ActivitiesListComponent } from './components/activities-list/activities-list.component';
import { ActivityProfileComponent } from './components/activity-profile/activity-profile.component';
import { ActivityInsertComponent } from './components/activity-insert/activity-insert.component';
import { GuideRegisterComponent } from './components/guide-register/guide-register.component';
import { ActivityEditComponent } from './components/activity-edit/activity-edit.component';
import { GuideFavoritesComponent } from './components/guide-favorites/guide-favorites.component';
import { authGuard } from './shared/guards/auth.guard';
import { adminRoleGuard } from './shared/guards/admin-role.guard';
import { guideRoleGuard } from './shared/guards/guide-role.guard';
import { NotAuthorizedComponent } from './components/not-authorized/not-authorized.component';
import { adminOrGuideRoleGuard } from './shared/guards/admin-or-guide-role.guard';
import { adminOrVisitorRoleGuard } from './shared/guards/admin-or-visitor-role.guard';

export const routes: Routes = [
  {
    path: '',
    title: 'Σωματείο Διπλωματούχων Ξεναγών',
    component: WelcomeComponent
  },
  {
    path: 'login',
    title: 'Είσοδος | Σωματείο Διπλωματούχων Ξεναγών',
    component: LoginComponent
  },
  {
    path: 'guides/register',
    title: 'Εγγραφή | Σωματείο Διπλωματούχων Ξεναγών',
    component: GuideRegisterComponent
  },
  {
    path: 'guides/list-of-licensed-tourist-guides',
    title: 'Κατάλογος Διπλωματούχων Ξεναγών',
    component: GuidesListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'guides/:uuid/account',
    title: 'Ο λογαριασμός μου | Σωματείο Διπλωματούχων Ξεναγών',
    component: GuideAccountComponent,
    canActivate: [authGuard, adminOrGuideRoleGuard]
  },
  {
    path: 'guides/:uuid/favorites',
    title: 'Αγαπημένα | Σωματείο Διπλωματούχων Ξεναγών',
    component: GuideFavoritesComponent,
    canActivate: [authGuard, guideRoleGuard]
  },
  {
    path: 'guides/:uuid',
    title: 'Προφίλ ξεναγού | Σωματείο Διπλωματούχων Ξεναγών',
    component: GuideProfileComponent,
    canActivate: [authGuard]
  },
  {
    path: 'visitors/register',
    title: 'Εγγραφή | Σωματείο Διπλωματούχων Ξεναγών',
    component: VisitorRegisterComponent
  },
  {
    path: 'visitors/list-of-visitors',
    title: 'Κατάλογος Επισκεπτών | Σωματείο Διπλωματούχων Ξεναγών',
    component: VisitorsListComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'visitors/:uuid/account',
    title: 'Ο λογαριασμός μου | Σωματείο Διπλωματούχων Ξεναγών',
    component: VisitorAccountComponent,
    canActivate: [authGuard, adminOrVisitorRoleGuard]
  },
  {
    path: 'activities/:uuid/edit',
    title: 'Δραστηριότητα | Επεξεργασία',
    component: ActivityEditComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'activities/list-of-activities',
    title: 'Δραστηριότητες | Σωματείο Διπλωματούχων Ξεναγών',
    component: ActivitiesListComponent,
    canActivate: [authGuard, adminOrGuideRoleGuard]
  },
  {
    path: 'activities/insert',
    title: 'Δραστηριότητα | Εισαγωγή',
    component: ActivityInsertComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'activities/:uuid',
    title: 'Δραστηριότητες | Σωματείο Διπλωματούχων Ξεναγών',
    component: ActivityProfileComponent,
    canActivate: [authGuard, adminOrGuideRoleGuard]
  },
  {
    path: 'regions/edit',
    title: 'Περιοχή | Επεξεργασία',
    component: RegionComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'languages/edit',
    title: 'Γλώσσα | Επεξεργασία',
    component: LanguageComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'categories/edit',
    title: 'Κατηγορία | Επεξεργασία',
    component: CategoryComponent,
    canActivate: [authGuard, adminRoleGuard]
  },
  {
    path: 'not-authorized',
    title: 'Σφάλμα',
    component: NotAuthorizedComponent,
  },
  {
    path: '**',
    title: 'Η σελίδα δε βρέθηκε',
    component: NotFoundComponent
  }
];
