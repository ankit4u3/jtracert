package com.google.code.jtracert;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Dmitry Bedrin
 */
public class ProjectInfoTest extends TestCase {

    @Test
    public void testPackageName() {

        String reflectionPackageName = ProjectInfo.class.getPackage().getName();
        String propertyPackageName = ProjectInfo.PROJECT_PACKAGE_NAME;

        assertEquals(reflectionPackageName,propertyPackageName);

    }

}
