import { Component, OnInit, inject, signal } from '@angular/core';
import { Medicament, OrdonnanceResult } from '../models';
import { SimulateurService } from '../simulateur.service';

@Component({
  selector: 'app-ordonnance',
  imports: [],
  templateUrl: './ordonnance.html',
  styleUrl: './ordonnance.scss',
})
export class OrdonnancePage implements OnInit {
  private readonly service = inject(SimulateurService);

  protected readonly medicaments = signal<Medicament[]>([]);
  protected readonly aChoisir = signal<string>('');
  protected readonly panier = signal<Medicament[]>([]);
  protected readonly parcours = signal(true);
  protected readonly ald = signal(false);
  protected readonly c2s = signal(false);
  protected readonly regimeLocal = signal(false);
  protected readonly franchiseDeja = signal(0);
  protected readonly resultat = signal<OrdonnanceResult | null>(null);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  ngOnInit(): void {
    this.service.lister().subscribe({
      next: (medicaments) => {
        this.medicaments.set(medicaments);
        if (medicaments.length) {
          this.aChoisir.set(medicaments[0].cip13);
        }
      },
      error: () => this.erreur.set('Catalogue indisponible.'),
    });
  }

  protected ajouter(): void {
    const med = this.medicaments().find((m) => m.cip13 === this.aChoisir());
    if (med) {
      this.panier.update((p) => [...p, med]);
      this.resultat.set(null);
    }
  }

  protected retirer(index: number): void {
    this.panier.update((p) => p.filter((_, i) => i !== index));
    this.resultat.set(null);
  }

  protected bascule(champ: 'parcours' | 'ald' | 'c2s' | 'regimeLocal', valeur: boolean): void {
    ({ parcours: this.parcours, ald: this.ald, c2s: this.c2s, regimeLocal: this.regimeLocal })[champ].set(valeur);
    this.resultat.set(null);
  }

  protected majFranchise(valeur: string): void {
    this.franchiseDeja.set(Number(valeur) || 0);
    this.resultat.set(null);
  }

  protected calculer(): void {
    if (!this.panier().length) {
      return;
    }
    this.chargement.set(true);
    this.erreur.set(null);
    this.service
      .calculerOrdonnance(
        this.panier().map((m) => m.cip13),
        {
          parcoursSoinsRespecte: this.parcours(),
          ald: this.ald(),
          c2s: this.c2s(),
          regimeLocal: this.regimeLocal(),
        },
        this.franchiseDeja(),
      )
      .subscribe({
        next: (resultat) => {
          this.resultat.set(resultat);
          this.chargement.set(false);
        },
        error: () => {
          this.erreur.set('Le calcul a échoué.');
          this.chargement.set(false);
        },
      });
  }

  protected euros(valeur: number): string {
    return valeur.toLocaleString('fr-FR', { style: 'currency', currency: 'EUR' });
  }
}
