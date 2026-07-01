import { Component, effect, input, signal } from '@angular/core';
import { Decompte } from './models';

@Component({
  selector: 'app-cascade-decompte',
  templateUrl: './cascade-decompte.html',
  styleUrl: './cascade-decompte.scss',
})
export class CascadeDecompte {
  readonly decompte = input.required<Decompte>();

  protected readonly revele = signal(false);
  protected readonly resteAnime = signal(0);

  private image = 0;

  constructor() {
    effect(() => {
      const d = this.decompte();
      this.revele.set(false);
      requestAnimationFrame(() => requestAnimationFrame(() => this.revele.set(true)));
      this.animerReste(d.resteACharge);
    });
  }

  protected largeur(part: number): number {
    const prix = this.decompte().prix || 1;
    return Math.min(100, (part / prix) * 100);
  }

  protected partReste(part: number): number {
    const reste = this.decompte().resteACharge || 1;
    return (part / reste) * 100;
  }

  protected euros(valeur: number): string {
    return valeur.toLocaleString('fr-FR', { style: 'currency', currency: 'EUR' });
  }

  private animerReste(cible: number): void {
    cancelAnimationFrame(this.image);
    const debut = performance.now();
    const duree = 650;
    const pas = (t: number) => {
      const progres = Math.min(1, (t - debut) / duree);
      const adouci = 1 - Math.pow(1 - progres, 4);
      this.resteAnime.set(cible * adouci);
      if (progres < 1) {
        this.image = requestAnimationFrame(pas);
      } else {
        this.resteAnime.set(cible);
      }
    };
    this.image = requestAnimationFrame(pas);
  }
}
