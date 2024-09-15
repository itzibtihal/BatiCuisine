package views;

public class UIFunctions {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static void printBorder(int width) {
        System.out.println(WHITE_BOLD + "+" + "-".repeat(width - 2) + "+" + RESET);
    }

    public static String centerText(String text, int width) {
        // Count the number of emojis in the text
        int emojiCount = text.replaceAll("[^\\p{So}\\p{Cn}]", "").length();

        // Adjust the width by subtracting extra space taken by emojis (assuming each emoji takes 2 chars)
        int adjustedWidth = width - (emojiCount * 2);

        int paddingSize = (adjustedWidth - text.length()) / 2;
        String padding = " ".repeat(Math.max(0, paddingSize));
        return padding + text + padding;
    }

    public static String addPadding(String option, int menuWidth) {
        int paddingSize = menuWidth - option.length() - 6;  // -6 for the menu item number and border
        return " ".repeat(Math.max(0, paddingSize));  // Prevent negative padding
    }
}
