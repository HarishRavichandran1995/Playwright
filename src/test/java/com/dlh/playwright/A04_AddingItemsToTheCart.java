package com.dlh.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class A04_AddingItemsToTheCart {

    static Playwright playwright;
    static Browser browser;
    BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
                        .setHeadless(false)
        );
    playwright.selectors().setTestIdAttribute("data-test");
    }

    @BeforeEach
    void setup() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Search for Pliers")
    @Test
    void searchForPliers() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        assertThat(page.locator(".card")).hasCount(4);

        List<String> productName = page.getByTestId("product-name").allTextContents();
        assertThat(productName).allMatch(name->name.contains("Pliers"));

        Locator outOfStock = page.locator(".card").filter(new Locator.FilterOptions().setHasText("Out of Stock"))
                .getByTestId("product-name");

        System.out.println("Out of Stock locator " + outOfStock);

        assertThat(outOfStock).hasCount(1);
        assertThat(outOfStock).hasText(" Long Nose Pliers ");

    }
}
