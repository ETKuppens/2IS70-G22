package com.example.cardhub.user_profile;

import android.widget.ImageView;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testSetProfilePicture()
    {
        Profile p = new Profile(new ImageView(null), "bob", 10, 10);
        boolean passed = false;
        try
        {
            p.setProfilePicture(null);
        } catch (NullPointerException e)
        {
            passed = true;
        }

        assert passed;
    }

    @Test
    public void testGetCardAmount()
    {
        Profile p = new Profile(new ImageView(null), "bob", 12, 10);
        assert p.getCardAmount().equals("12");
    }

    @Test
    public void testGetTradeAmount()
    {
        Profile p = new Profile(new ImageView(null), "bob", 10, 5);
        String s = p.getTradeAmount();
        assert p.getTradeAmount().equals("5");
    }

    @Test
    public void testGetUsername()
    {
        Profile p = new Profile(new ImageView(null), "bob", 10, 10);
        assert p.getUsername().equals("bob");
    }
}
