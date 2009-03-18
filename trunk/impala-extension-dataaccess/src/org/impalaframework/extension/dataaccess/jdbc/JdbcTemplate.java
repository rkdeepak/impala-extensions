package org.impalaframework.extension.dataaccess.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;

/**
 * Extension of {@link JdbcTemplate} which contains method
 * {@link #queryForList(String, Map)}, which takes named arguments in the form
 * of a {@link Map} instance
 * 
 * @author Phil Zoio
 */
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

	/**
	 * Returns list of maps as in {@link #queryForList(String, Object[])}.
	 * Equivalent to {@link #queryForList(String, Map, boolean)}, with the final
	 * argument set to false
	 * 
	 * @param sql
	 *            the sql which contains named arguments, by default in the form
	 *            ${name}, which will be replaced by ? before being submitted to
	 *            the JDBC driver
	 * @param parameters
	 *            a map of parameter arguments
	 * @return a list of maps as in {@link #queryForList(String, Object[])}
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> queryForList(String sql,
			Map<String, Object> parameters) throws DataAccessException {
		return queryForList(sql, parameters, false);
	}

	/**
	 * Returns list of maps as in {@link #queryForList(String, Object[])}.
	 * 
	 * @param sql
	 *            the sql which contains named arguments, by default in the form
	 *            ${name}, which will be replaced by ? before being submitted to
	 *            the JDBC driver
	 * @param parameters
	 *            a map of parameter arguments
	 * @param resultKeysAsLowerCase
	 *            if true all result keys will be returned as lower case
	 * @return a list of maps as in {@link #queryForList(String, Object[])}
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryForList(String sql,
			Map<String, Object> parameters, boolean resultKeysAsLowerCase)
			throws DataAccessException {

		final JdbcArguments convert = converter.convert(sql, parameters);
		final ColumnMapRowMapper rowMapper = resultKeysAsLowerCase ? new ColumnMapRowMapper() {

			@Override
			protected String getColumnKey(String columnName) {
				return super.getColumnKey(columnName).toLowerCase();
			}

		}
				: new ColumnMapRowMapper();

		return query(convert.getSql(), convert.getArguments(), rowMapper);
	}

	public void setParameterPrefix(String parameterPrefix) {
		this.parameterPrefix = parameterPrefix;
	}

	public void setParameterSuffix(String parameterSuffix) {
		this.parameterSuffix = parameterSuffix;
	}
}
