//package com.tracker.tasktracker.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class AuthInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        // 1. Set headers to prevent caching of PROTECTED pages
//        // This forces the browser to ask the server (and thus run this interceptor) every time
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
//        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
//        response.setDateHeader("Expires", 0); // Proxies
//
//        HttpSession session = request.getSession(false);
//        boolean loggedIn = (session != null && session.getAttribute("user") != null);
//
//        String uri = request.getRequestURI();
//
//        // Allow sign-in and static resources to pass through
//        boolean publicPath =
//                uri.equals("/sign-in") ||
//                        uri.startsWith("/css/") ||
//                        uri.startsWith("/js/") ||
//                        uri.startsWith("/images/");
//
//        // Protect only /employee/**
//        boolean protectedEmployeePath = uri.startsWith("/employee/");
//
//        if (protectedEmployeePath && !loggedIn && !publicPath) {
//            response.sendRedirect("/sign-in");
//            return false; // stop processing
//        }
//
//        return true; // continue
//    }
//
//}
