package si.roskar.diploma.server.DB;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.google.gwt.i18n.client.Constants;

public class DBSource{
	public static DataSource getDataSource(){
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("org.postgresql.Driver");
		basicDataSource.setUrl(DBProperties.getProperty("DB.URL"));
		basicDataSource.setUsername(DBProperties.getProperty("DB.USERNAME"));
		basicDataSource.setPassword(DBProperties.getProperty("DB.PASSWORD"));
		return basicDataSource;
	}
}