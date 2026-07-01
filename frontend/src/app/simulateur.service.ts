import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Decompte, Medicament, Profil } from './models';

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
}
