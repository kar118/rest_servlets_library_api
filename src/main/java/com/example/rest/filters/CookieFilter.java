package com.example.rest.filters;

import com.example.rest.beans.User;
import com.example.rest.responses.ExceptionResponse;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@WebFilter(filterName = "LoginFilter", urlPatterns = {"/dashboard/*"})
public class CookieFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            Object userObj = req.getSession().getAttribute("user");
            if(userObj == null) {
                throw new Exception("Unauthorized user");
            }
            User user = (User) userObj;
            System.out.println(user);
            if(!checkForUserIdCookie(req.getCookies(), user)) {
                throw new Exception("No proper cookie");
            }
            chain.doFilter(request, response);
        } catch (Exception ex) {
            Gson gson = new Gson();
            response.setContentType("application/json;charset=UTF-8");
            ExceptionResponse exResponse = new ExceptionResponse();
            exResponse.setMessage(ex.getLocalizedMessage());
            exResponse.setStatus(401);
            ((HttpServletResponse) response).setStatus(401);
            gson.toJson(exResponse, response.getWriter());
        }
    }
    private boolean checkForUserIdCookie(Cookie[] cookies, User user) {
        for (Cookie cookie : cookies) {

            if ("userId".equals(cookie.getName())) {
                return new String(Base64.getDecoder()
                        .decode(cookie.getValue().getBytes()))
                        .equals(user.getLogin());
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
