package si.roskar.diploma.shared;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter{
	
	@Override
	public void destroy(){
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		Date now = new Date();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.setDateHeader("Date", now.getTime());
		
		// expires in one day
		httpResponse.setDateHeader("Expires", now.getTime() - 86400000L);
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException{
	}
}
