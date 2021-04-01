package com.example.rest.servlets;

import com.example.rest.beans.Role;
import com.example.rest.beans.User;
import com.example.rest.request.LoginRequest;
import com.example.rest.responses.ExceptionResponse;
import com.example.rest.responses.Response;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

@WebServlet(name = "LoginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {
    private HashMap<String, String> userDatabase;

    @Override
    public void init() throws ServletException {
        userDatabase = new HashMap<>();
        userDatabase.put("user", "user");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");

        try {
            LoginRequest loginRequest = gson.fromJson(request.getReader(), LoginRequest.class);
            validateRequest(loginRequest);
            Boolean isAdmin = isAdmin(loginRequest.getLogin());
            Response loginResponse;

            if(isAdmin)
                loginResponse = adminLogin(loginRequest, request, response);
            else
                loginResponse = userLogin(loginRequest, request, response);

            response.setStatus(loginResponse.getStatus());
            gson.toJson(loginResponse, response.getWriter());

        } catch (Exception ex) {
            ExceptionResponse exResponse = new ExceptionResponse();
            exResponse.setMessage(ex.getLocalizedMessage());
            exResponse.setStatus(500);
            response.setStatus(500);
            gson.toJson(exResponse, response.getWriter());
        }
    }

    private void validateRequest(LoginRequest loginRequest) throws Exception {
        if(loginRequest.getLogin() == null || loginRequest.getPassword() == null)
            throw new Exception("Invalid data!");
    }

    private boolean isAdmin(String login) {
        if("admin".equals(login))
            return true;
        return false;
    }

    private Response adminLogin(LoginRequest loginRequest, HttpServletRequest req, HttpServletResponse res) {
        if ("admin".equals(loginRequest.getPassword())) {

            User admin = new User(loginRequest.getLogin(), loginRequest.getPassword(), Role.ADMIN);
            addUserToHttpSession(admin, req);
            addUserCookie(admin, res);

            return new Response("Logged successfully", 200);
        } else
            return new Response("Incorrect data!", 400);
    }

    private Response userLogin(LoginRequest loginRequest, HttpServletRequest req, HttpServletResponse res) {
        if (userDatabase.containsKey(loginRequest.getLogin()) && userDatabase.get(loginRequest.getLogin()).equals(loginRequest.getPassword())) {

            User user = new User(loginRequest.getLogin(), loginRequest.getPassword(), Role.USER);
            addUserToHttpSession(user, req);
            addUserCookie(user, res);

            return new Response("Logged successfully!", 200);
        } else
            return new Response("Incorrect data!", 400);
    }

    private void addUserToHttpSession(User user, HttpServletRequest req) {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("user", user);
    }

    private void addUserCookie(User user, HttpServletResponse res) {
        res.addCookie(new Cookie("userId", getBase64FromString(user.getLogin())));
    }

    private String getBase64FromString(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
}
