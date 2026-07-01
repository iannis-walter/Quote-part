import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Decompte } from './models';
import { SimulateurService } from './simulateur.service';

describe('SimulateurService', () => {
  let service: SimulateurService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(SimulateurService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('poste un calcul avec le code et le profil', () => {
    const profil = { parcoursSoinsRespecte: true, ald: false };
    let recu: Decompte | undefined;

    service.calculer('3400920095517', profil).subscribe((d) => (recu = d));

    const requete = httpMock.expectOne('/api/calculs');
    expect(requete.request.method).toBe('POST');
    expect(requete.request.body).toEqual({ cip13: '3400920095517', profil });

    requete.flush({
      prix: 10, baseRemboursement: 10, tauxPourcent: 65,
      remboursementSecu: 6.5, ticketModerateur: 3.5, franchiseMedicale: 1, resteACharge: 4.5,
    });

    expect(recu?.resteACharge).toBe(4.5);
  });
});
