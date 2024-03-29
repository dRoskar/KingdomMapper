package si.roskar.diploma.server.security;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import si.roskar.diploma.server.DAO.UserJDBCTemplate;
import si.roskar.diploma.server.DB.DBSource;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	private UserJDBCTemplate	userJdbcTemplate	= null;
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		userJdbcTemplate = new UserJDBCTemplate();
		userJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		String dbPassword = userJdbcTemplate.getUserHash(authentication.getName());
		String username = authentication.getName();
		
		logger.info("User {} is trying to log in", username);
		
		if(dbPassword != null){
			try{
				if(BCrypt.checkpw(authentication.getCredentials().toString(), dbPassword)){
					logger.info("User {} authenticated successfully", username);
					
					List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
					
					if(userJdbcTemplate.isAdmin(username)){
						grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
						grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
					}
					else{
						grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
					}
					
					Authentication auth = new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString(), grantedAuthorities);
					return auth;
				}else{
					// wrong password
					logger.info("User {} failed to authenticate", authentication.getName());
					return null;
				}
			}catch(IllegalArgumentException e){
				logger.error("ERROR!", e);
				return null;
			}
		}else{
			// user doesn't exist
			logger.info("user doesn't exist");
			return null;
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication){
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
