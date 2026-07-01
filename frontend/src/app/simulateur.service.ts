import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Decompte, Medicament, OrdonnanceResult, Profil, Source } from './models';

@Injectable({ providedIn: 'root' })
export class SimulateurService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api';

  lister(): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.base}/medicaments`);
  }

  rechercher(terme: string): Observable<Medicament[]> {
    return this.http.get<Medicament[]>(`${this.base}/medicaments`, { params: { q: terme } });
  }

  calculer(cip13: string, profil: Profil, complementaire: number | null = null): Observable<Decompte> {
    return this.http.post<Decompte>(`${this.base}/calculs`, { cip13, profil, complementaire });
  }

  calculerOrdonnance(cip13s: string[], profil: Profil, franchiseDejaConsommee: number): Observable<OrdonnanceResult> {
    return this.http.post<OrdonnanceResult>(`${this.base}/calculs/ordonnance`, {
      cip13s,
      profil,
      franchiseDejaConsommee,
    });
  }

  source(): Observable<Source> {
    return this.http.get<Source>(`${this.base}/source`);
  }
}
