create table presentation (
    cip13        varchar(13)   primary key,
    cis          varchar(8)    not null,
    prix         numeric(10, 2) not null,
    taux         integer,
    remboursable boolean       not null
);
