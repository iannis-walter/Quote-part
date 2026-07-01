import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CascadeDecompte } from '../cascade-decompte';
import { Decompte, Medicament } from '../models';
import { SimulateurService } from '../simulateur.service';

const LIBELLES_SMR: Record<string, string> = {
  IMPORTANT: 'SMR important',
  MODERE: 'SMR modéré',
  FAIBLE: 'SMR faible',
  INSUFFISANT: 'SMR insuffisant',
};

@Component({
  selector: 'app-simulateur',
  imports: [CascadeDecompte],
  templateUrl: './simulateur.html',
  styleUrl: './simulateur.scss',
})
export class SimulateurPage implements OnInit {
  private readonly service = inject(SimulateurService);

  protected readonly medicaments = signal<Medicament[]>([]);
  protected readonly cip13 = signal<string>('');
  protected readonly parcours = signal(true);
  protected readonly ald = signal(false);
  protected readonly c2s = signal(false);
  protected readonly regimeLocal = signal(false);
  protected readonly mutuelle = signal(0);
  protected readonly decompte = signal<Decompte | null>(null);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  protected readonly tagSmr = computed(() => {
    const choisi = this.medicaments().find((m) => m.cip13 === this.cip13());
    if (!choisi) {
      return null;
    }
    return choisi.remboursable ? (LIBELLES_SMR[choisi.smr ?? ''] ?? null) : 'Non remboursable';
  });

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

  protected chercher(terme: string): void {
    const source = terme.trim() ? this.service.rechercher(terme.trim()) : this.service.lister();
    source.subscribe({
      next: (medicaments) => {
        this.medicaments.set(medicaments);
        this.cip13.set(medicaments.length ? medicaments[0].cip13 : '');
        this.decompte.set(null);
      },
      error: () => this.erreur.set('Recherche impossible.'),
    });
  }

  protected basculerParcours(valeur: boolean): void {
    this.parcours.set(valeur);
    this.decompte.set(null);
  }

  protected basculerAld(valeur: boolean): void {
    this.ald.set(valeur);
    this.decompte.set(null);
  }

  protected basculerC2s(valeur: boolean): void {
    this.c2s.set(valeur);
    this.decompte.set(null);
  }

  protected basculerRegimeLocal(valeur: boolean): void {
    this.regimeLocal.set(valeur);
    this.decompte.set(null);
  }

  protected choisirMutuelle(pourcentage: number): void {
    this.mutuelle.set(pourcentage);
    this.decompte.set(null);
  }

  protected calculer(): void {
    if (!this.cip13()) {
      return;
    }
    this.chargement.set(true);
    this.erreur.set(null);
    this.service
      .calculer(
        this.cip13(),
        {
          parcoursSoinsRespecte: this.parcours(),
          ald: this.ald(),
          c2s: this.c2s(),
          regimeLocal: this.regimeLocal(),
        },
        this.mutuelle() > 0 ? this.mutuelle() : null,
      )
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
