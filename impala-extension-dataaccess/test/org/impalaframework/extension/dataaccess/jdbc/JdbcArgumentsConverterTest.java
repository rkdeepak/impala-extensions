package org.impalaframework.extension.dataaccess.jdbc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.extension.dataaccess.jdbc.JdbcArguments;
import org.impalaframework.extension.dataaccess.jdbc.JdbcArgumentsConverter;
import org.impalaframework.extension.dataaccess.jdbc.JdbcArgumentsConverter.SqlChars;

public class JdbcArgumentsConverterTest extends TestCase {

	private JdbcArgumentsConverter converter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		converter = new JdbcArgumentsConverter();
	}
	
	public void testGetParamNames() {
		checkParamNames(converter, Arrays.asList("param1", "param2"), "select * from table where param1 = ${param1} and param2 = ${param2}");
		checkParamNames(converter, Arrays.asList("param1", "param2"), "select * from table where param1 = ${param1} and param2 = ${param2} order by param1");
	}

	public void testSimple() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("param1", "value1");
		params.put("param2", "value2");
		checkConvert(converter, 
				params,
				"select * from table where param1 = ${param1} and param2 = ${param2}",
				new Object[] {"value1", "value2"},
				"select * from table where param1 = ? and param2 = ?");
	}
	
	public void testRepeatedArgs() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("param1", "value1");
		params.put("param2", "value2");
		checkConvert(converter, 
				params,
				"select * from table where param1 = ${param1} and param2 = ${param2} where param3 > ${param1}",
				new Object[] {"value1", "value2", "value1"},
				"select * from table where param1 = ? and param2 = ? where param3 > ?");
	}
	
	public void testArgsNotAtEnd() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("param1", "value1");
		checkConvert(converter, 
				params,
				"select * from table where param1 = ${param1} and more stuff",
				new Object[] {"value1"},
				"select * from table where param1 = ? and more stuff");
	}
	
	public void testNoArgs() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		
		checkConvert(converter, 
				params,
				"select * from table where param1 = 'param'",
				new Object[0],
				"select * from table where param1 = 'param'");
	}
	
	private void checkConvert(JdbcArgumentsConverter converter, Map<String, Object> params, String startSql, Object[] args, String endSql) {

		final JdbcArguments converted = converter.convert(startSql, params);
		assertEquals(endSql, converted.getSql());
		assertTrue(Arrays.equals(args, converted.getArguments()));
	}

	private void checkParamNames(JdbcArgumentsConverter converter, final List<String> expected,
			final String sql) {
		final SqlChars chars = converter.new SqlChars(sql);
		final List<String> paramNames = converter.getParamNames(chars);
		
		assertEquals(expected, paramNames);
	}

}
