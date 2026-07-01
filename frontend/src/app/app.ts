import { Component, OnInit, inject, signal } from '@angular/core';
import { CascadeDecompte } from './cascade-decompte';
import { Decompte, Medicament } from './models';
import { SimulateurService } from './simulateur.service';

@Component({
  selector: 'app-root',
  imports: [CascadeDecompte],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  private readonly service = inject(SimulateurService);

  protected readonly medicaments = signal<Medicament[]>([]);
  protected readonly cip13 = signal<string>('');
  protected readonly parcours = signal(true);
  protected readonly ald = signal(false);
  protected readonly decompte = signal<Decompte | null>(null);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  ngOnInit(): void {
    this.service.lister().subscribe({
      next: (medicaments) => {
        this.medicaments.set(medicaments);
        if (medicaments.length) {
          this.cip13.set(medicaments[0].cip13);
        }
      },
      error: () => this.erreur.set('Impossible de charger le catalogue.'),
    });
  }

  protected choisir(cip13: string): void {
    this.cip13.set(cip13);
    this.decompte.set(null);
  }

  protected basculerParcours(valeur: boolean): void {
    this.parcours.set(valeur);
    this.decompte.set(null);
  }

  protected basculerAld(valeur: boolean): void {
    this.ald.set(valeur);
    this.decompte.set(null);
  }

  protected calculer(): void {
    if (!this.cip13()) {
      return;
    }
    this.chargement.set(true);
    this.erreur.set(null);
    this.service
      .calculer(this.cip13(), { parcoursSoinsRespecte: this.parcours(), ald: this.ald() })
      .subscribe({
        next: (decompte) => {
          this.decompte.set(decompte);
          this.chargement.set(false);
        },
        error: () => {
          this.erreur.set('Le calcul a échoué.');
          this.chargement.set(false);
        },
      });
  }
}
