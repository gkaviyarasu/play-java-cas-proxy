package controllers;

import com.google.inject.Inject;
import play.mvc.Controller;
import store.CacheSessionStore;

public class CasController extends Controller{

    @Inject
    CacheSessionStore sessionStore;

    protected CasUserProfile getUserProfile() {
        Object o = sessionStore.get(ctx(),"userProfile");
        if(o instanceof CasUserProfile) {
            return (CasUserProfile)o;
        } else {
            return null;
        }
    }
}
