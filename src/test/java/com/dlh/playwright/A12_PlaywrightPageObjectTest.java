package com.dlh.playwright;

import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import playwrightObjects.NavBar;
import playwrightObjects.ProductDetails;
import playwrightObjects.ProductList;
import playwrightObjects.SearchComponent;

import java.util.Arrays;
import java.util.List;

public class A12_PlaywrightPageObjectTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setupBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    public void setup() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    public void closeContext() {
        browserContext.close();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com/");
    }

    @DisplayName("Search for Tape")
    @Test
    void searchTape() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("tape");
        var productNames = productList.getProductNames();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Tape"));
    }

    @DisplayName("Add to Cart")
    @Test
    void addToCart() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        ProductDetails productDetails = new ProductDetails(page);
        NavBar navBar = new NavBar(page);

        searchComponent.searchBy("Pliers");
        productList.viewProductDetails("Combination Pliers");
        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();
        navBar.openCart();
    }
}
