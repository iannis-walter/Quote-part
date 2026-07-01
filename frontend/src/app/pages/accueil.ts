import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-accueil',
  imports: [RouterLink],
  templateUrl: './accueil.html',
  styleUrl: './accueil.scss',
})
export class AccueilPage {
  protected readonly capacites = [
    'Taux selon le SMR',
    'ALD à 100 %',
    'Parcours de soins',
    'Ticket modérateur',
    'Franchise plafonnée',
    'C2S',
    'Régime Alsace-Moselle',
    'Tarif forfaitaire (génériques)',
    'Complémentaire santé',
    'Ordonnance complète',
  ];
}
