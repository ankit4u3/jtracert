package com.google.code.jtracert;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 * @author Dmitry.Bedrin@gmail.com
 */
public class ProjectInfoTest extends TestCase {

    /**
     * 
     */
    @Test
    public void testPackageName() {

        String reflectionPackageName = ProjectInfo.class.getPackage().getName();
        String propertyPackageName = ProjectInfo.PROJECT_PACKAGE_NAME;

        assertEquals(reflectionPackageName,propertyPackageName);

    }

}
