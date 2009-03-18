package org.impalaframework.extension.dataaccess.jdbc;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a mechanism for converting an SQL statement with named parameters and arguments in
 * the form of a map, into a {@link JdbcArguments} object.
 * 
 * @author Phil Zoio
 */
public class JdbcArgumentsConverter {

	public JdbcArguments convert(String sql, Map<String, Object> params) {

		SqlChars chars = new SqlChars(sql);
		final List<String> paramNames = getParamNames(chars);
		final List<Object> arguments = new LinkedList<Object>();
		
		for (String paramName : paramNames) {
			if (!params.containsKey(paramName)) {
				//FIXME test
				throw new IllegalArgumentException("Missing parameter '" + paramName + "' for query '" + sql + "'");			
			}

			arguments.add(params.get(paramName));
		}
		
		final Object[] argsArray = arguments.toArray(new Object[0]);
		return new JdbcArguments(chars.getReplacementSql(), argsArray);
		
	}

	List<String> getParamNames(SqlChars chars) {
		List<String> paramNames = new LinkedList<String>();
		
		String nextParam = null;
		while ((nextParam = chars.nextParameter()) != null) {
			paramNames.add(nextParam);
		}
		
		return paramNames;
	}
	
    class SqlChars {
    	
    	private StringBuffer buffer;
		private String sql;
		private int position = 0;

		public SqlChars(String sql) {
			super();
			this.sql = sql;
			this.buffer = new StringBuffer();
		}
		
		String nextParameter() {
			
			if (position >= sql.length()) {
				return null;
			}
			
			int startIndex = sql.indexOf("${", position);
			if (startIndex >= 0) {
				int endIndex = sql.indexOf("}", startIndex+1);
				if (endIndex >= 0) {
					buffer.append(sql.substring(position, startIndex));
					buffer.append("?");
					position = endIndex+1;
					return sql.substring(startIndex+2, endIndex);
				}
				else {
					//FIXME test
					throw new IllegalArgumentException("Unclosed parameter in '" + sql + "'. Missing } character new position " + position);
				}
			}
			
			return null;
		}
		
		public String getReplacementSql() {
			return buffer.toString();
		}
		
	}
	
}
