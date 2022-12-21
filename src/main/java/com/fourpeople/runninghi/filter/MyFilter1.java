package com.fourpeople.runninghi.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);

            if (headerAuth.equals("cos")) {
                chain.doFilter(req, res);  // 다음으로 넘겨주기 위해 필요
            } else {
                // 컨트롤러까지 안 가고 여기에서 중단됨
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
                out.write("not authenticated");
                // println이나 wrtie나 같은 결과
            }
        }

    }
}
