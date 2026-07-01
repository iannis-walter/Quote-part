-- Base de remboursement distincte du prix (ex. tarif forfaitaire de responsabilité des génériques).
-- Nullable : quand elle est absente, la base est assimilée au prix.
alter table presentation add column base_remboursement numeric(10, 2);
