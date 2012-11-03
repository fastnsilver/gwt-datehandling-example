package me.fns.gwt.datehandling.client;

import junit.framework.Test;
import junit.framework.TestCase;
import me.fns.gwt.datehandling.client.util.CSTimeUtilTestGwt;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * <p>
 * GWT Date Handling Example Test Suite.
 * </p>
 * <p>
 * See <a href=
 * "https://developers.google.com/web-toolkit/doc/latest/DevGuideTesting#DevGuideJUnitSuites"
 * >Dev Guide Testing</a>.
 * </p>
 * 
 * @author cphillipson
 * 
 */
public class GwtExampleTestSuite extends TestCase {

	public static Test suite() {
		// group related GWT component tests by name
		final GWTTestSuite suite = new GWTTestSuite("GWT-based tests for date handling example.");
		suite.addTestSuite(CSTimeUtilTestGwt.class);
		return suite;
	}
}
