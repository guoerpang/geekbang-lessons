package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.RestController;
import org.geektimes.web.mvc.myannotation.MyController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author GH
 */
@Path("/register")
@MyController
public class RegisterController implements RestController {

    @Resource
    private UserService userService;

    @GET
    @Path("/registerSave")
    public String registerSave(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(parameterMap.get("name")[0]);
        user.setEmail(parameterMap.get("email")[0]);
        user.setPassword(parameterMap.get("password")[0]);
        user.setPhoneNumber(parameterMap.get("phoneNumber")[0]);

        userService.register(user);
        return "注册成功";
    }

    @GET
    @Path("/getAll")
    public List<User> getAll(HttpServletRequest request, HttpServletResponse response){

        List<User> users = userService.getAll();
        return users;
    }


}
