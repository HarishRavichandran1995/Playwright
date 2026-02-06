package playwrightObjects;

import com.microsoft.playwright.Page;

import java.util.List;

public class ProductList {

    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }


    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }

    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    public String getSearchCompletedMessage() {
        return page.getByTestId("search_completed").textContent();
    }
}
