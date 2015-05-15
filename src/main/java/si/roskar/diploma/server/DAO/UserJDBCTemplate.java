package si.roskar.diploma.server.DAO;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserJDBCTemplate{
	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplateObject = null;
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void insert(String username, String password){
		String SQL = "INSERT INTO public.\"User\"(name, password) VALUES (?, ?)";
		
		jdbcTemplateObject.update(SQL, username, password);
		return;
	}
}