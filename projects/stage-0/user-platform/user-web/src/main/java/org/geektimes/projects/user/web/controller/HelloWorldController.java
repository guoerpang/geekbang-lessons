package org.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.myannotation.MyController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 输出 “Hello,World” Controller
 */

@Path("/hello")
@MyController
public class HelloWorldController implements PageController {

    @GET
//    @POST
    @Path("/index") //  /hello/world -> HelloWorldController
    public String index(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "index.jsp";
    }

    /**
     * 跳转到注册页面
     */
    @GET
    @Path("/register")
    public String register(HttpServletRequest request, HttpServletResponse response){
        return "register.jsp";
    }

    /**
     * 跳转到注册成功页面
     */
    @GET
    @Path("/registerSave")
    public String registerSave(HttpServletRequest request, HttpServletResponse response){

        return "registerSave.jsp";
    }


}
