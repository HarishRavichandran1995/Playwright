package com.dlh.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(A05_HeadlessChromeOptions.class)
public class A05_AddingItemsToTheCart {

    @DisplayName("Search for Pliers")
    @Test
    void searchForPliers(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        assertThat(page.locator(".card")).hasCount(4);

        List<String> productName = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productName).allMatch(name->name.contains("Pliers"));

        Locator outOfStock = page.locator(".card").filter(new Locator.FilterOptions().setHasText("Out of Stock"))
                .getByTestId("product-name");

        System.out.println("Out of Stock locator " + outOfStock);

        assertThat(outOfStock).hasCount(1);
        assertThat(outOfStock).hasText(" Long Nose Pliers ");

    }
}
