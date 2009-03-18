package org.impalaframework.extension.dataaccess.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;

public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {
	
	private JdbcArgumentsConverter converter;
	
	private String parameterPrefix = null;
	private String parameterSuffix = null;
	
	public JdbcTemplate() {
		super();
	}

	public JdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		if (this.parameterPrefix == null)
			this.parameterPrefix = "${";
		if (this.parameterSuffix == null)
			this.parameterSuffix = "}";
		converter = new JdbcArgumentsConverter(parameterPrefix, parameterSuffix);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>queryForList(String sql, Map<String,Object> parameters) throws DataAccessException {
		
		final JdbcArguments convert = converter.convert(sql, parameters);
		return query(convert.getSql(), convert.getArguments(), new ColumnMapRowMapper() {

			@Override
			protected String getColumnKey(String columnName) {
				return super.getColumnKey(columnName).toLowerCase();
			}
			
		});
	}

	public void setParameterPrefix(String parameterPrefix) {
		this.parameterPrefix = parameterPrefix;
	}
	
	public void setParameterSuffix(String parameterSuffix) {
		this.parameterSuffix = parameterSuffix;
	}
}
