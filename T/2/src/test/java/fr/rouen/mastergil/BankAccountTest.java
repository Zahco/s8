package fr.rouen.mastergil;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by spaurgeo on 13/02/17.
 */
public class BankAccountTest {
    @Test
    public void creerCompte() throws Exception {
        //Given
        BankAccount bankAccount = new BankAccount();
        //When
        bankAccount.creerCompte();
        //Then
        assertThat(bankAccount.solde).isNotNull();
    }

    @Test
    public void creerCompteWithArguments() throws Exception {
        //Given
        BankAccount bankAccount = new BankAccount();
        int montant = 2;
        Devise devise = Devise.DOLLAR;
        //When
        bankAccount.creerCompte(montant, devise);
        //Then
        assertThat(bankAccount.solde).isNotNull();
        assertThat(bankAccount.solde.getMontant()).isEqualTo(montant);
        assertThat(bankAccount.solde.getDevise()).isEqualTo(devise);
    }

    @Test
    public void consulterSolde() throws Exception {
        //Given
        BankAccount bankAccount = new BankAccount();
        int montant = 2;
        Devise devise = Devise.DOLLAR;
        //When
        bankAccount.creerCompte(montant, devise);
        //Then
        assertThat(bankAccount.consulterSolde()).isEqualTo("Votre solde actuel est de 2 DOLLAR");
    }

    @Test
    public void deposerArgent() throws Exception {
        //Given
        BankAccount bankAccount = new BankAccount();
        int montant = 2;
        Devise devise = Devise.DOLLAR;
        //When
        bankAccount.creerCompte(montant, devise);
        bankAccount.deposerArgent(montant);
        //Then
        assertThat(bankAccount.consulterSolde()).isEqualTo("Votre solde actuel est de 4 DOLLAR");
    }

    @Test
    public void retirerArgent() throws Exception {

    }

    @Test
    public void isSoldePositif() throws Exception {

    }

    @Test
    public void transfererArgent() throws Exception {

    }

}