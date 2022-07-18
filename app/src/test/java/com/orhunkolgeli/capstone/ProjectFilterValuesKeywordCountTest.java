package com.orhunkolgeli.capstone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.orhunkolgeli.capstone.utils.ProjectFilterValues;

import org.junit.Test;

public class ProjectFilterValuesKeywordCountTest {
    @Test
    public void sizeAfterAddition_isCorrect() {
        ProjectFilterValues projectFilterValues = new ProjectFilterValues();
        projectFilterValues.addKeyword("Hello");
        projectFilterValues.addKeyword("hello");
        projectFilterValues.addKeyword("world");
        int expectedSize = 2;
        int actualSize = projectFilterValues.getKeywords().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void maxKeywordCountReached_isCorrect() {
        ProjectFilterValues projectFilterValues = new ProjectFilterValues();
        int count = ProjectFilterValues.MAX_KEYWORD_COUNT;
        for (int i = 0; i < count - 1; i++) {
            projectFilterValues.addKeyword("keyword " + i);
        }
        assertFalse(projectFilterValues.maxKeywordCountReached());
        projectFilterValues.addKeyword("hello");
        assertTrue(projectFilterValues.maxKeywordCountReached());
    }
}
