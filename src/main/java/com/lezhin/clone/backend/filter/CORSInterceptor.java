// package com.lezhin.clone.backend.filter;

// import java.io.IOException;

// import org.springframework.stereotype.Component;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.annotation.WebFilter;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @WebFilter
// @Component
// public class CORSInterceptor implements Filter {
//     private static final String[] allowedOrigins = {
//         "http://localhost:3000"
//     };

//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//         HttpServletRequest request = (HttpServletRequest) servletRequest;

//         String requestOrigin = request.getHeader("Origin");
//         if(isAllowedOrigin(requestOrigin)) {
//             // Authorize the origin, all headers, and all methods
//             ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", requestOrigin);
//             ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "*");
//             ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods",
//                     "GET, OPTIONS, HEAD, PUT, POST, DELETE");

//             HttpServletResponse resp = (HttpServletResponse) servletResponse;

//             // CORS handshake (pre-flight request)
//             if (request.getMethod().equals("OPTIONS")) {
//                 resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//                 return;
//             }
//         }
//         // pass the request along the filter chain
//         filterChain.doFilter(request, servletResponse);
//     }

//     private boolean isAllowedOrigin(String origin){
//         for (String allowedOrigin : allowedOrigins) {
//             if(origin.equals(allowedOrigin)) return true;
//         }
//         return false;
//     }
// }