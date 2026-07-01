import { Routes } from '@angular/router';
import { AccueilPage } from './pages/accueil';
import { DonneesPage } from './pages/donnees';
import { OrdonnancePage } from './pages/ordonnance';
import { SimulateurPage } from './pages/simulateur';

export const routes: Routes = [
  { path: '', component: AccueilPage, title: 'Reliquat — reste à charge médicamenteux' },
  { path: 'simulateur', component: SimulateurPage, title: 'Simulateur · Reliquat' },
  { path: 'ordonnance', component: OrdonnancePage, title: 'Ordonnance · Reliquat' },
  { path: 'donnees', component: DonneesPage, title: 'Données · Reliquat' },
  { path: '**', redirectTo: '' },
];
