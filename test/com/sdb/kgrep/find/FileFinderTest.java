/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sdb.kgrep.find;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author debin.sun
 */
public class FileFinderTest {
    
    public FileFinderTest() {
    }

    /**
     * Test of visitFile method, of class FileFinder.
     */
    @Test
    public void testVisitFile() {
        System.out.println("visitFile");
        Path file = null;
        BasicFileAttributes attrs = null;
        FileFinder instance = new FileFinder(FileFinder.FindType.GLOB, "*.java");
        FileVisitResult expResult = null;
        FileVisitResult result = instance.visitFile(file, attrs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of preVisitDirectory method, of class FileFinder.
     */
    @Test
    public void testPreVisitDirectory() {
        System.out.println("preVisitDirectory");
        Path dir = null;
        BasicFileAttributes attrs = null;
        FileFinder instance = null;
        FileVisitResult expResult = null;
        FileVisitResult result = instance.preVisitDirectory(dir, attrs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of visitFileFailed method, of class FileFinder.
     */
    @Test
    public void testVisitFileFailed() {
        System.out.println("visitFileFailed");
        Path file = null;
        IOException exc = null;
        FileFinder instance = null;
        FileVisitResult expResult = null;
        FileVisitResult result = instance.visitFileFailed(file, exc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFound method, of class FileFinder.
     */
    @Test
    public void testGetFound() {
        System.out.println("getFound");
        FileFinder instance = null;
        List<Path> expResult = null;
        List<Path> result = instance.getFound();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFailed method, of class FileFinder.
     */
    @Test
    public void testGetFailed() {
        System.out.println("getFailed");
        FileFinder instance = null;
        List<Path> expResult = null;
        List<Path> result = instance.getFailed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
