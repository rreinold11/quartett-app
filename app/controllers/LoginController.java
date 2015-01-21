package controllers;

import authentication.MyLoginHandler;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.*;
import contracts.login.*;
import contracts.model.IUser;
import play.data.*;

import java.util.Map;
import java.util.UUID;

public class LoginController extends Controller{

    //GET /login
    public static Result showLoginForm() {

    	String message = "";
    	
        return ok(views.html.login.render(message));
    }

    //GET /logout
    public static Result logout(){
    	
    	//clear session data
		session().clear();
    	
		//redirect to index page
    	//return redirect("/");
		return redirect(routes.controllers.Application.index());
    }
    

	// POST /login
	public static Result authenticate() {
		Form<Credentials> loginForm = Form.form(Credentials.class)
				.bindFromRequest();
		
		Map<String,String> data = loginForm.data();
		
		Credentials c = new Credentials();
		
		//get form fields
		c.email    = data.get("email");
		c.password = data.get("password");
		
		//check if user exists
		LoginHandler lh = new MyLoginHandler();
		IUser user = lh.authenticate(c.email, c.password);

		//user does not exist or is unauthorized
		if (user == null) {
			return unauthorized(views.html.login.render("Invalid username or password"));
		}
		
		//store session data
		session().clear();
		session("pid", user.getID().toString());

		//redirect to game
		return redirect("/");
	}
	
}
