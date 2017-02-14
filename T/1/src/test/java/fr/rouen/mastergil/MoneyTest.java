package fr.rouen.mastergil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by spaurgeo on 06/02/17.
 */
public class MoneyTest {

    @Test
    public void createMoneyZeroPeso() {
        Money money = new Money();
        assert(money.getMontant() == 0);
        assert(money.getDevise().equals(Devise.PESO));
    }

    @Test
    public void isPositif() throws Exception {

    }

    @Test
    public void getMontant() throws Exception {

    }

    @Test
    public void setMontant() throws Exception {

    }

    @Test
    public void getDevise() throws Exception {

    }

    @Test
    public void setDevise() throws Exception {

    }

    @Test
    public void increaseMontant() throws Exception {

    }

    @Test
    public void decreaseMontant() throws Exception {

    }

}