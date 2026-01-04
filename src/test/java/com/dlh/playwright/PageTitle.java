package com.dlh.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageTitle {

    @Test
    void shouldShowThePageTitle() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();
        Page page = browser.newPage();
        page.navigate("https://testautomationpractice.blogspot.com/");
        String title = page.title();
        System.out.println(title);
        Assertions.assertTrue(title.contains("Automation Testing Practice"));
        browser.close();
        playwright.close();
    }
}
