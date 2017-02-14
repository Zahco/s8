package fr.rouen.mastergil;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by spaurgeo on 06/02/17.
 */
public class MoneyTest {

    @Test
    public void createMoneyZeroPeso() {
        //Given
        Money money;
        //When
        money = new Money();
        //Then
        assertThat(money.getMontant() == 0).isTrue();
        assertThat(money.getDevise()).isEqualTo(Devise.PESO);
    }

    @Test
    public void createMoneyWithValue() {
        //Given
        Money money;
        int montant = 2;
        Devise devise = Devise.EURO;
        //When
        money = new Money(montant, devise);
        //Then
        assertThat(money.getMontant() == montant).isTrue();
        assertThat(money.getDevise()).isEqualTo(devise);
    }

    @Test
    public void isPositiveValue() {
        //Given
        Money money;
        //When
        money = new Money(2, Devise.EURO);
        //Then
        assertThat(money.isPositif()).isTrue();
    }
    @Test
    public void isNegativeValue() {
        //Given
        Money money;
        //When
        money = new Money(-2, Devise.EURO);
        //Then
        assertThat(money.isPositif()).isFalse();
    }
    @Test
    public void setMontant() {
        //Given
        Money money = new Money(-2, Devise.EURO);
        //When
        money.setMontant(2);
        //Then
        assertThat(money.getMontant()).isEqualTo(2);
    }
    @Test
    public void getMontant() {
        //Given
        Money money = new Money(-2, Devise.EURO);
        //When
        //Then
        assertThat(money.getMontant()).isEqualTo(-2);
    }
    @Test
    public void getDevise() {
        //Given
        Money money = new Money(3, Devise.EURO);
        //When
        //Then
        assertThat(money.getDevise()).isEqualTo(Devise.EURO);
    }
    @Test
    public void setDevise() {
        //Given
        Money money = new Money(-2, Devise.DOLLAR);
        //When
        money.setDevise(Devise.DOLLAR);
        //Then
        assertThat(money.getDevise()).isEqualTo(Devise.DOLLAR);
    }
    @Test
    public void setNullDevice() {
        //Given
        Money money = new Money(-2, Devise.DOLLAR);
        //When
        //Then
        try {
            money.setDevise(null);
            fail("Impossible");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Devise doit être spécifiée");
        }
    }
    @Test
    public void increaseMontantWithDollar() {
        //Given
        Money money;
        int montant = 2;
        Devise devise = Devise.DOLLAR;
        //When
        money = new Money(montant, devise);
        money.increaseMontant(3);
        //Then
        assertThat(money.getMontant()).isEqualTo(6);
    }
    @Test
    public void increaseMontantWithYen() {
        //Given
        Money money;
        int montant = 2;
        Devise devise = Devise.YEN;
        //When
        money = new Money(montant, devise);
        money.increaseMontant(3);
        //Then
        assertThat(money.getMontant()).isEqualTo(17);
    }

    @Test
    public void decreaseMontantWithDollar() {
        //Given
        Money money;
        int montant = 2;
        Devise devise = Devise.YEN;
        //When
        money = new Money(montant, devise);
        money.decreaseMontant(3);
        //Then
        assertThat(money.getMontant()).isEqualTo(-2);
    }
    @Test
    public void decreaseMontantWithDinar() {
        //Given
        Money money;
        int montant = 8;
        Devise devise = Devise.DINAR;
        //When
        money = new Money(montant, devise);
        money.decreaseMontant(2);
        //Then
        assertThat(money.getMontant()).isEqualTo(8);
    }


}