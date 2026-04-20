package com.dlh.playwright;

import com.microsoft.playwright.*;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class A11_PlaywrightRestAPITest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu")
        ));
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();

        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").waitFor();
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

    @DisplayName("Playwrigth allows us to mock API responses")
    @Nested
    class MockingAPIResponses {
        @Test
        @DisplayName("When a search returns a single product")
        void whenASingleItemsFound() {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(A11_MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                    .setStatus(200))
            );
            var searchBox = page.getByPlaceholder("Search");
            searchBox.fill("pliers");
            searchBox.press("Enter");
            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")
                    .filter(new Locator.FilterOptions().setHasText("Super Pliers")))
                    .isVisible();
        }
    }


}
