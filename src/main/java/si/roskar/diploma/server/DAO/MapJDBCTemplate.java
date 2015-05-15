package si.roskar.diploma.server.DAO;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class MapJDBCTemplate{
	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplateObject = null;
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void insert(String name, int userId){
		String SQL = "insert into Map (name, user_id) values (?, ?)";
		
		jdbcTemplateObject.update(SQL, name, userId);
		return;
	}
}