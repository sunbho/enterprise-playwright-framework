package com.playwright.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Page object for DemoQA Checkbox tree component.
 *
 * <p>Provides methods to interact with checkbox tree nodes using role-based
 * and CSS selectors. Extends BasePage for common Playwright interactions.</p>
 */
public final class CheckBoxPage extends BasePage {

    private static final String CHECKBOX_TREE_WRAPPER = ".check-box-tree-wrapper";
    private static final String RESULT_SECTION = "#result";

    /**
     * Creates a CheckBox page object backed by an active Playwright page.
     *
     * @param page active Playwright page
     */
    public CheckBoxPage(Page page) {
        super(page);
    }

    /**
     * Navigates to the Checkbox section URL.
     */
    public void navigateToCheckBoxPage() {
        navigate("https://demoqa.com/checkbox");
    }

    /**
     * Checks if the checkbox tree is visible on the page.
     */
    public boolean isCheckboxTreeVisible() {
        return isVisible(CHECKBOX_TREE_WRAPPER);
    }

    /**
     * Builds a role-based selector for a checkbox by its label.
     *
     * @param labelName the checkbox label name (e.g., "Home", "Desktop", "Select Home")
     * @return the role-based selector string
     */
    private String buildCheckboxSelector(String labelName) {
        // If label doesn't start with "Select", prepend it
        String fullLabel = labelName.startsWith("Select") ? labelName : "Select " + labelName;
        return "role=checkbox[name='" + fullLabel + "']";
    }

    /**
     * Clicks a checkbox by its node name.
     * Waits for visibility before clicking.
     *
     * @param labelName the checkbox label (e.g., "Home", "Desktop", "Notes")
     */
    public void selectCheckbox(String labelName) {
        String selector = buildCheckboxSelector(labelName);
        clickWhenVisible(selector);
    }

    /**
     * Verifies if a checkbox is checked.
     *
     * @param labelName the checkbox label
     * @return true if aria-checked="true", false otherwise
     */
    public boolean isCheckboxChecked(String labelName) {
        String selector = buildCheckboxSelector(labelName);
        Locator checkbox = getLocator(selector);
        String ariaChecked = checkbox.getAttribute("aria-checked");
        return "true".equals(ariaChecked);
    }

    /**
     * Gets a locator for a checkbox by its label name (useful for assertions).
     *
     * @param labelName the checkbox label
     * @return Locator for the checkbox
     */
    public Locator getCheckboxLocator(String labelName) {
        String selector = buildCheckboxSelector(labelName);
        return getLocator(selector);
    }

    /**
     * Gets the count of checked checkboxes in the tree.
     */
    public int getCheckedCheckboxCount() {
        return getLocator("role=checkbox[aria-checked='true']").count();
    }

    /**
     * Gets the total count of all checkboxes in the tree.
     */
    public int getTotalCheckboxCount() {
        return getLocator("role=checkbox").count();
    }

    /**
     * Expands or collapses a tree node by clicking the expand/collapse switcher.
     *
     * @param nodeLabel the visible label of the node (e.g., "Home", "Documents")
     */
    public void toggleNodeExpansion(String nodeLabel) {
        String toggleSelector = String.format("role=treeitem[name='%s'] >> .rc-tree-switcher", nodeLabel);
       getLocator(toggleSelector).isEnabled();
       getLocator(toggleSelector).click();
    }

    /**
     * Selects multiple checkboxes by their labels.
     *
     * @param labelNames variable number of checkbox labels
     */
    public void selectMultipleCheckboxes(String... labelNames) {
        for (String labelName : labelNames) {
            selectCheckbox(labelName);
        }
    }

    /**
     * Verifies multiple checkboxes are checked.
     *
     * @param labelNames variable number of checkbox labels
     * @return true if all specified checkboxes are checked, false otherwise
     */
    public boolean areCheckboxesChecked(String... labelNames) {
        for (String labelName : labelNames) {
            if (!isCheckboxChecked(labelName)) {
                return false;
            }
        }
        return true;
    }


    private String buildTreeNodeSelector(String nodeLabel) {
    String safeLabel = nodeLabel == null ? "" : nodeLabel.replace("'", "\\'");
    // Find the tree title element with the label text, then go to parent treeitem
    return String.format(".rc-tree-title:has-text('%s') >> xpath=ancestor::div[@role='treeitem'][1]", safeLabel);
    }

    private String buildTreeSwitcherSelector(String nodeLabel) {
        String safeLabel = nodeLabel == null ? "" : nodeLabel.replace("'", "\\'");
        // Find the tree title, go to parent treeitem, then find the switcher
        return String.format(".rc-tree-title:has-text('%s') >> xpath=ancestor::div[@role='treeitem'][1] >> .rc-tree-switcher", safeLabel);
    }

    /**
     * Expands a tree node if it is currently collapsed.
     * If the node is already expanded or is a leaf node, does nothing.
     *
     * @param nodeLabel the visible label of the tree node (e.g., "Home", "Documents")
     */
    public void expandNode(String nodeLabel) {
        String itemSelector = buildTreeNodeSelector(nodeLabel);
        logger.info("Expanding node with selector: {}", itemSelector);

        // Get the treeitem and check if it's expandable
        Locator item = waitForVisibleLocator(itemSelector);
        String expandedAttr = item.getAttribute("aria-expanded");

        // Only proceed if the node is expandable and currently collapsed
        if (expandedAttr != null && "false".equalsIgnoreCase(expandedAttr)) {
            clickWhenVisible(buildTreeSwitcherSelector(nodeLabel));
        }
    }

    /**
     * Collapses a tree node if it is currently expanded.
     * If the node is already collapsed or is a leaf node, does nothing.
     *
     * @param nodeLabel the visible label of the tree node (e.g., "Home", "Documents")
     */
    public void collapseNode(String nodeLabel) {
        String itemSelector = buildTreeNodeSelector(nodeLabel);
        logger.info("Collapsing node with selector: {}", itemSelector);

        // Get the treeitem and check if it's expandable
        Locator item = waitForVisibleLocator(itemSelector);
        String expandedAttr = item.getAttribute("aria-expanded");

        // Only proceed if the node is expandable and currently expanded
        if (expandedAttr != null && "true".equalsIgnoreCase(expandedAttr)) {
            clickWhenVisible(buildTreeSwitcherSelector(nodeLabel));
        }
    }

    /** * Verifies that the result section contains the expected selected items. *
     * @param expectedItems comma-separated items that should be in the result * @return true if all expected items are found in the result text */
    public boolean verifySelectedItems(String... expectedItems) {
        String resultText = getText(RESULT_SECTION).toLowerCase();
        logger.info("Verifying selected items in result: {}", resultText);

        for (String item : expectedItems) {
            if (!resultText.contains(item.toLowerCase())) {
                logger.warn("Expected item '{}' not found in result", item);
                return false;
            }
        }
        return true;
    }
}