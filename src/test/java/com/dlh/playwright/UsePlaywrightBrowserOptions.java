package com.dlh.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright(UsePlaywrightBrowserOptions.MyOptions.class)
public class UsePlaywrightBrowserOptions {
    public static class MyOptions implements OptionsFactory {

        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu")));
        }
    }

    @Test
    void shouldSearchByKeyword(Page page){
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Pliers");
        page.locator("button:has-text('Search')").click();
        int matchingSearchResults = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResults>0);
    }
}
