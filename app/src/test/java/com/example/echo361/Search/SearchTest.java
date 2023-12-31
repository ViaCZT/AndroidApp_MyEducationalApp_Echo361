package com.example.echo361.Search;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Objects;

public class SearchTest {

    @Test(timeout = 1000)
    public void isWordTest() {
        Search Search = new Search();

        // check the lowercase
        assertTrue(Search.isWord('a'));

        // check the uppercase
        assertTrue(Search.isWord('A'));

        // check the symbles
        assertFalse(Search.isWord('$'));

        // check the numbers
        assertFalse(Search.isWord('1'));
    }

    @Test(timeout = 1000)
    public void inputToCourseTest() {
        Search Search = new Search();

        // check the empty
        assertArrayEquals(new String[]{"",""}, Search.inputToCourse(""));

        // check the case only have course code
        assertArrayEquals(new String[]{"","1234"}, Search.inputToCourse("1234"));

        // check the case only have college code
        assertArrayEquals(new String[]{"COMP",""}, Search.inputToCourse("COMP"));

        // check the case only have college code and course code
        assertArrayEquals(new String[]{"COMP","1234"}, Search.inputToCourse("COMP1234"));

        // check the lowercase
        assertArrayEquals(new String[]{"comp","1234"}, Search.inputToCourse("comp1234"));

        // check the college code more than 4 letters without course code
        assertArrayEquals(new String[]{"comp",""}, Search.inputToCourse("comppppp"));

        // check the college code more than 4 letters with course code
        assertArrayEquals(new String[]{"comp","1234"}, Search.inputToCourse("comppppp1234"));

        // check the college code less than 4 letters without course code
        assertArrayEquals(new String[]{"co",""}, Search.inputToCourse("co"));

        // check the college code less than 4 letters with course code
        assertArrayEquals(new String[]{"co","1234"}, Search.inputToCourse("co1234"));

        // check the course code more than 4 digits without college code
        assertArrayEquals(new String[]{"","1234"}, Search.inputToCourse("123456"));

        // check the college code more than 4 digits with course code
        assertArrayEquals(new String[]{"comp","1234"}, Search.inputToCourse("comp123456"));

        // check the course code less than 4 digits without course code
        assertArrayEquals(new String[]{"","12"}, Search.inputToCourse("12"));

        // check the college code less than 4 digits with course code
        assertArrayEquals(new String[]{"comp","12"}, Search.inputToCourse("comp12"));

        //check the case both the college code and the course code have more than 4 chars
        assertArrayEquals(new String[]{"comp","1234"}, Search.inputToCourse("comppppp1234567"));

        //check the case both the college code and the course code have less than 4 chars
        assertArrayEquals(new String[]{"com","12"}, Search.inputToCourse("com12"));

    }

    @Test(timeout = 1000)
    public void isInCollegeTest() {
        Search Search = new Search();

        // check a right case with uppercase and lowercase
        assertTrue(Search.isInCollege("Comp"));

        // check a right case with uppercase
        assertTrue(Search.isInCollege("COMP"));

        // check a right case with lowercase
        assertTrue(Search.isInCollege("COM"));

        // check a right case with uppercase and less than 4 letter
        assertTrue(Search.isInCollege("COM"));

        // check a right case with lowercase and less than 4 letter
        assertTrue(Search.isInCollege("bio"));

        // check a case if the parameter more than 4 letter;
        assertFalse(Search.isInCollege("biology"));

        // check a case if the parameter is consists of digits
        assertFalse(Search.isInCollege("biology"));

        // check a case if the parameter has non-letter char
        assertFalse(Search.isInCollege("Co44"));

        // check another case if the parameter has non-letter char
        assertFalse(Search.isInCollege("Com&"));

    }

    @Test(timeout = 1000)
    public void getCollegeTest() {
        Search Search = new Search();

        // test empty input
        assertNull(Search.getCollege(""));

        // test a wrong 4 letters input
        ArrayList<String> getCollegeTest = new ArrayList<>();
        assertArrayEquals(getCollegeTest.toArray(), Objects.requireNonNull(Search.getCollege("COME")).toArray());

        // test a correct 4 letters input
        ArrayList<String> getCollegeTest1 = new ArrayList<>();
        getCollegeTest1.add("COMP");
        assertArrayEquals(getCollegeTest1.toArray(), Objects.requireNonNull(Search.getCollege("COMP")).toArray());

        // test a correct input less than 4 letters
        ArrayList<String> getCollegeTest2 = new ArrayList<>();
        getCollegeTest2.add("COMP");
        assertArrayEquals(getCollegeTest2.toArray(), Objects.requireNonNull(Search.getCollege("COM")).toArray());

        // test a correct input has letter
        ArrayList<String> getCollegeTest3 = new ArrayList<>();
        getCollegeTest3.add("CBEA");
        getCollegeTest3.add("CHEM");
        getCollegeTest3.add("COMP");
        assertArrayEquals(getCollegeTest3.toArray(), Objects.requireNonNull(Search.getCollege("C")).toArray());
    }


    @Test(timeout = 1000)
    public void indexOfSpaceTest() {
        Search Search = new Search();

        // input is empty
        ArrayList<Integer> indexOfSpcetest= new ArrayList<>();
        assertArrayEquals(indexOfSpcetest.toArray(),Search.indexOfSpace("").toArray());

        // input hasn't non-letter char
        assertArrayEquals(indexOfSpcetest.toArray(),Search.indexOfSpace("Yitao").toArray());

        // the non-letter char is at head
        assertArrayEquals(indexOfSpcetest.toArray(),Search.indexOfSpace("Yitao").toArray());

        // the non-letter char is at head and the non-letter char is a space
        ArrayList<Integer> indexOfSpcetest1= new ArrayList<>();
        indexOfSpcetest1.add(0);
        assertArrayEquals(indexOfSpcetest1.toArray(),Search.indexOfSpace(" Yitao").toArray());

        // the non-letter char is at end and the non-letter char is a symbol
        ArrayList<Integer> indexOfSpcetest2= new ArrayList<>();
        indexOfSpcetest2.add(5);
        assertArrayEquals(indexOfSpcetest2.toArray(),Search.indexOfSpace("Yitao%").toArray());

        // the non-letter char is at middle and the non-letter char is a space
        ArrayList<Integer> indexOfSpcetest3= new ArrayList<>();
        indexOfSpcetest3.add(2);
        assertArrayEquals(indexOfSpcetest3.toArray(),Search.indexOfSpace("Yi tao").toArray());

        // more than one the non-letter char and the non-letter chars are symbols
        ArrayList<Integer> indexOfSpcetest4= new ArrayList<>();
        indexOfSpcetest4.add(2);
        indexOfSpcetest4.add(5);
        assertArrayEquals(indexOfSpcetest4.toArray(),Search.indexOfSpace("Yi%ta%o").toArray());

    }

    @Test(timeout = 1000)
    public void getNameTest() {
        Search Search = new Search();
        // input is empty
        String[] fALNameTest = new String[]{"", ""};
        assertArrayEquals(fALNameTest, Search.getName(""));

        // no interval
        String[] fALNameTest1 = new String[]{"Yitao", ""};
        assertArrayEquals(fALNameTest1, Search.getName("Yitao"));

        // one interval by space
        String[] fALNameTest2 = new String[]{"Yitao","Zhang"};
        assertArrayEquals(fALNameTest2, Search.getName("Yitao Zhang"));

        // one interval by symbol
        String[] fALNameTest3 = new String[]{"Yitao", "Zhang"};
        assertArrayEquals(fALNameTest3, Search.getName("Yitao$Zhang"));

        // tow interval by space
        String[] fALNameTest4 = new String[]{"Yi", "Zhang"};
        assertArrayEquals(fALNameTest4, Search.getName("Yi tao Zhang"));

        // three intervals by symbols
        String[] fALNameTest5 = new String[]{"Yit", "ang"};
        assertArrayEquals(fALNameTest5, Search.getName("Yit$ao$Zh$ang"));
    }






}