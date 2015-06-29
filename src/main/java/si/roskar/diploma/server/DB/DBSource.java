package si.roskar.diploma.server.DB;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import si.roskar.diploma.server.util.ApplicationProperties;

import com.google.gwt.i18n.client.Constants;

public class DBSource{
	public static DataSource getDataSource(){
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("org.postgresql.Driver");
		basicDataSource.setUrl(ApplicationProperties.getProperty("DB.URL"));
		basicDataSource.setUsername(ApplicationProperties.getProperty("DB.USERNAME"));
		basicDataSource.setPassword(ApplicationProperties.getProperty("DB.PASSWORD"));
		return basicDataSource;
	}
}