package si.roskar.diploma.server.DAO;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class VectorJDBCTemplate{
	private DataSource		dataSource			= null;
	private JdbcTemplate	jdbcTemplateObject	= null;
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public boolean deletePointsInLayer(int layerId){
		final String SQL = "DELETE FROM public.\"Point\" where layer_id = " + layerId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean deleteLinesInLayer(int layerId){
		final String SQL = "DELETE FROM public.\"Line\" where layer_id = " + layerId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
	
	public boolean deletePoligonsInLayer(int layerId){
		final String SQL = "DELETE FROM public.\"Polygon\" where layer_id = " + layerId;
		
		jdbcTemplateObject.update(SQL);
		
		return true;
	}
}
