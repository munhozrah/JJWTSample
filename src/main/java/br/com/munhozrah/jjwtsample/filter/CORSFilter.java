package br.com.munhozrah.jjwtsample.filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CORSFilter implements Filter {

	public CORSFilter() { }

	public void init(FilterConfig fConfig) throws ServletException { }

	public void destroy() {	}

	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			HttpServletResponse res = (HttpServletResponse) response;

			res.addHeader("Access-Control-Allow-Origin", "*");
			res.addHeader("Access-Control-Allow-Credentials", "true");
			res.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST , PUT, DELETE, OPTIONS");
			res.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Headers, Access-Control-Allow-Origin, Authorization");
			res.addHeader("Access-Control-Expose-Headers", "Authorization");
			
			//res.addHeader("Access-Control-Allow-Headers", "Authorization, Access-Control-Allow-Origin, Origin, X-Requested-With, Content-Type, Accept, ClienteApp");

			// HTTP 1.1
			res.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			// HTTP 1.0
			res.addHeader("Pragma", "no-cache");

			res.addDateHeader("Expires", 0); // Proxies
			
			chain.doFilter(request, response);
	}
}
