package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.swagger;

public class Application extends Controller
{
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result swagger() {
        //return redirect("/assets/lib/swagger-ui/index.html?url=" +
        //        (request().secure()? "://https":"http://") + request().host() + "/api-docs");
        return ok(swagger.render());

    }

}
