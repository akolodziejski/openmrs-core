package org.openmrs.module;


import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Ignore;
import org.openmrs.test.BaseContextSensitiveTest;

public class ModuleFileParserTest extends BaseContextSensitiveTest{
	
	
	private ModuleFileParser moduleFileParser;

	@Before
	public  void runBeforeTest(){
		String fileName = "org/openmrs/module/include/TestModuleForParseTest.xml";
		InputStream moduleFileStream = ModuleFileParserTest.class.getClassLoader().getResourceAsStream(fileName);
		moduleFileParser = new ModuleFileParser(moduleFileStream);
	}
	
	/**
	 * @see ModuleFileParser#parse()
	 * @verifies parse start-before modules
	 */
	@Ignore
	public void parse_shouldParseStartbeforeModules() throws Exception {
		
		Module module = moduleFileParser.parse();
		assertNotNull(module.getStartBeforeModulesMap());
	}
}