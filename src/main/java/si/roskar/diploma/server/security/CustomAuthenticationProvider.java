package si.roskar.diploma.server.security;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
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
	
	private UserJDBCTemplate	userJdbcTemplate	= null;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		userJdbcTemplate = new UserJDBCTemplate();
		userJdbcTemplate.setDataSource(DBSource.getDataSource());
		
		String dbPassword = userJdbcTemplate.getUserHash(authentication.getName());
		
		if(dbPassword != null){
			try{
				if(BCrypt.checkpw(authentication.getCredentials().toString(), dbPassword)){
					List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
					Authentication auth = new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString(), grantedAuthorities);
					return auth;
				}else{
					// wrong password
					return null;
				}
			}catch(IllegalArgumentException e){
				e.printStackTrace();
				return null;
			}
		}else{
			// user doesn't exist
			return null;
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication){
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
