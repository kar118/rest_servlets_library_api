package com.example.rest.filters;

import com.example.rest.beans.Role;
import com.example.rest.beans.User;
import com.example.rest.responses.ExceptionResponse;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/dashboard/*"})
public class AdminFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getMethod().equals("DELETE") || httpServletRequest.getMethod().equals("POST")) {
            try {
                User user = (User) httpServletRequest.getSession().getAttribute("user");

                if (!Role.ADMIN.equals(user.getRole()))
                    throw new Exception();

                chain.doFilter(request, response);
            } catch (Exception ex) {
                Gson gson = new Gson();
                response.setContentType("application/json;charset=UTF-8");
                ExceptionResponse exResponse = new ExceptionResponse();
                exResponse.setMessage("Unauthorized user");
                exResponse.setStatus(401);
                ((HttpServletResponse) response).setStatus(401);
                gson.toJson(exResponse, response.getWriter());
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
