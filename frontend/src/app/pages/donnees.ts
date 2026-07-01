import { Component, OnInit, inject, signal } from '@angular/core';
import { Source } from '../models';
import { SimulateurService } from '../simulateur.service';

@Component({
  selector: 'app-donnees',
  imports: [],
  templateUrl: './donnees.html',
  styleUrl: './donnees.scss',
})
export class DonneesPage implements OnInit {
  private readonly service = inject(SimulateurService);

  protected readonly source = signal<Source | null>(null);

  protected readonly bareme = [
    { smr: 'SMR important', taux: '65 %' },
    { smr: 'SMR modéré', taux: '30 %' },
    { smr: 'SMR faible', taux: '15 %' },
    { smr: 'SMR insuffisant', taux: '0 %' },
  ];

  ngOnInit(): void {
    this.service.source().subscribe({ next: (s) => this.source.set(s) });
  }
}
