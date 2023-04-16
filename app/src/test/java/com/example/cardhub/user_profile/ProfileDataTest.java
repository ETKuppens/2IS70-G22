package com.example.cardhub.user_profile;

import com.google.firebase.auth.FirebaseAuth;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Map;

public class ProfileDataTest extends TestCase {
    FirebaseAuth auth;
    ProfileData data;

    public ProfileDataTest() {
        auth = Mockito.mock(FirebaseAuth.class);
        auth.signInWithEmailAndPassword("unit@test.com", "123123");
        data = new ProfileData(new ProfileRepository() {
            @Override
            public void receiverProfile(Map<String, Object> profile) {

            }
        });
    }


    public void testLogOut() {
        data.logOut();
        assertNull(auth.getCurrentUser());
    }

    public void testRequestProfile() {
        data.requestProfile();
    }
}