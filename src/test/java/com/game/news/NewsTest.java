package com.game.news;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewsTest {

    private News news;
    @Before
    public void setUp() throws Exception {
        news = new News();
    }

    @Test
    public void testGetNewsContent() {
        String expected = "Cooler Pay Gains Add to Debate on When Fed Might Pause Rate Hikes!";
        String actual = news.getNewsContent(1);
        assertEquals(expected, actual);

        expected = "Boeing to set up a new 737 MAX assembly line in Everett!";
        actual = news.getNewsContent(2);
        assertEquals(expected, actual);

        expected = "JPMorgan says CEO Jamie Dimon is having emergency heart surgery!";
        actual = news.getNewsContent(5);
        assertEquals(expected, actual);

        expected = "Justice Dept. Sues to Block $13 Billion Deal by UnitedHealth Group!";
        actual = news.getNewsContent(8);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNewsContent_OutOfBounds() {
        String expected = "News not found";
        String actual = news.getNewsContent(20);
        assertEquals(expected, actual);

        actual = news.getNewsContent(-1);
        assertEquals(expected, actual);
    }
}