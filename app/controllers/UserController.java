package controllers;

import controllers.annotation.Authenticated;
import play.mvc.Result;

/**
 * Created by kavi on 2/2/16.
 */
public class UserController extends CasController{

    public Result logout() {
        sessionStore.remove(ctx(), "userProfile");
        ctx().session().clear();
        ctx().args.remove("CasSessionId");
        return ok();
    }

    public Result callback() {
        return ok();
    }

}
