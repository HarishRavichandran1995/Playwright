package com.dlh.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchToolUsingBeforeAndAfter {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterEach
    void tearDown(){
        browser.close();
        playwright.close();
    }

    @Test
    void shouldSearchByKeyword(){
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResults>0);
    }
}
