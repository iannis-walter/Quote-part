export type Smr = 'IMPORTANT' | 'MODERE' | 'FAIBLE' | 'INSUFFISANT';

export interface Medicament {
  cip13: string;
  denomination: string | null;
  prix: number;
  remboursable: boolean;
  smr: Smr | null;
}

export interface Profil {
  parcoursSoinsRespecte: boolean;
  ald: boolean;
  c2s: boolean;
  regimeLocal: boolean;
}

export interface Decompte {
  prix: number;
  baseRemboursement: number;
  tauxPourcent: number;
  remboursementSecu: number;
  ticketModerateur: number;
  franchiseMedicale: number;
  resteACharge: number;
  resteApresComplementaire: number | null;
}
