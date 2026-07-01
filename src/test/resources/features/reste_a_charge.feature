# language: fr
Fonctionnalité: Calcul du reste à charge médicamenteux
  Afin de savoir combien un médicament va réellement me coûter
  En tant que patient
  Je veux connaître le reste à charge selon ma situation

  Scénario: SMR important, dans le parcours de soins
    Étant donné une présentation remboursable à 10,00 € avec un SMR "important"
    Et un patient dans le parcours de soins, sans ALD
    Quand je calcule le reste à charge
    Alors le taux appliqué est 65 %
    Et le remboursement de la Sécurité sociale est 6,50 €
    Et le reste à charge est 4,50 €

  Scénario: Patient en affection longue durée
    Étant donné une présentation remboursable à 20,00 € avec un SMR "important"
    Et un patient en ALD
    Quand je calcule le reste à charge
    Alors le taux appliqué est 100 %
    Et le reste à charge est 1,00 €

  Scénario: Médicament non remboursable
    Étant donné une présentation non remboursable à 8,00 €
    Quand je calcule le reste à charge
    Alors le reste à charge est 8,00 €

  Scénario: Hors du parcours de soins, le remboursement est minoré
    Étant donné une présentation remboursable à 10,00 € avec un SMR "important"
    Et un patient hors du parcours de soins, sans ALD
    Quand je calcule le reste à charge
    Alors le remboursement de la Sécurité sociale est 3,90 €
    Et le reste à charge est 7,10 €

  Scénario: Générique sous tarif forfaitaire de responsabilité
    Étant donné une présentation remboursable à 3,45 € avec une base de remboursement de 3,00 € et un SMR "important"
    Et un patient dans le parcours de soins, sans ALD
    Quand je calcule le reste à charge
    Alors le remboursement de la Sécurité sociale est 1,95 €
    Et le reste à charge est 2,50 €

  Scénario: Bénéficiaire de la Complémentaire santé solidaire
    Étant donné une présentation remboursable à 10,00 € avec un SMR "important"
    Et un patient bénéficiaire de la C2S
    Quand je calcule le reste à charge
    Alors le reste à charge est 0,00 €

  Scénario: Régime local d'Alsace-Moselle
    Étant donné une présentation remboursable à 10,00 € avec un SMR "important"
    Et un patient relevant du régime local d'Alsace-Moselle
    Quand je calcule le reste à charge
    Alors le taux appliqué est 90 %
    Et le remboursement de la Sécurité sociale est 9,00 €
    Et le reste à charge est 2,00 €
