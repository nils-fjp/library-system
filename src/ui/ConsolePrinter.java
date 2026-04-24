package ui;

import java.util.LinkedHashMap;
import java.util.Map;

import static ui.ANSI.*;
import static ui.Constants.*;

public class ConsolePrinter {

    private static final String ANSI_ESCAPE_REGEX = "\u001B\\[[;\\d?]*[ -/]*[@-~]";

    public static void printStyledHeader(String headerText, String ansiTextColor, String ansiLineColor) {
        headerText = fit(safe(headerText), INNER_WIDTH);
        System.out.println();
        System.out.println(formatHeaderLine(ansiTextColor + headerText + RESET, ansiLineColor));
    }

    public static void printHeader(String text) {
        text = fit(safe(text), INNER_WIDTH);
        System.out.println();
        System.out.println(formatHeaderLine(BOLD + text + NO_BOLD + DEFAULT_FG, BRIGHT_BLACK));
    }

    public static void printFooter() {
        System.out.println(" ".repeat(MARGIN) + BRIGHT_BLACK + "─".repeat(OUTER_WIDTH) + DEFAULT_FG);
    }

    public static void printField(String key, Object value) {
        String keyWithSeparator = fit(safe(key), Math.max(0, GAP - 2)) + ":";
        String paddedKey = padRight(keyWithSeparator, Math.max(0, GAP - 1));
        System.out.println(" ".repeat(MARGIN + 1) + YELLOW + paddedKey + " " + DEFAULT_FG + value);
    }

    public static void printPrompt(String message) {
        message = fit(safe(message), INNER_WIDTH);
        String centerFlex = " ".repeat((INNER_WIDTH - visibleLength(message)) / 4);
        System.out.println(" ".repeat(MARGIN) + YELLOW + message + centerFlex + DEFAULT_FG);
    }

    public static void printPromptInline(String message) {
        message = safe(message);
        System.out.print(" ".repeat(MARGIN) + YELLOW + message + DEFAULT_FG);
    }

    public static void printFields(String title, LinkedHashMap<String, Object> fields) {
        printHeader(title);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            printField(entry.getKey(), entry.getValue());
        }

        printFooter();
    }

    public static void printError(String message) {
        message = safe(message);
        System.out.println();
        System.out.println(" ".repeat(MARGIN + 1) + BRIGHT_RED + message + DEFAULT_FG);
        System.out.println();
    }

    public static void printSuccess(String message) {
        message = safe(message);
        System.out.println();
        System.out.println(" ".repeat(MARGIN + 1) + BRIGHT_GREEN + message + DEFAULT_FG);
        System.out.println();
    }

    static String center(String text, int width) {
        text = fit(safe(text), width);
        int visibleWidth = visibleLength(text);
        String leftPadding = " ".repeat(Math.max(0, (width - visibleWidth) / 2));
        String rightPadding = " ".repeat(Math.max(0, width - visibleWidth - leftPadding.length()));
        return leftPadding + text + rightPadding;
    }

    static String padRight(String text, int width) {
        text = fit(safe(text), width);
        return text + " ".repeat(Math.max(0, width - visibleLength(text)));
    }

    static String formatTwoColumnLine(String leftText, String rightText, int width) {
        leftText = safe(leftText);
        rightText = safe(rightText);

        int rightWidth = Math.clamp(width - 1, 0, visibleLength(rightText));
        int leftWidth = Math.max(0, width - rightWidth - 1);
        String fittedLeftText = fit(leftText, leftWidth);
        int spacesBetween = Math.max(1, width - visibleLength(fittedLeftText) - rightWidth);

        return fittedLeftText + " ".repeat(spacesBetween) + rightText;
    }

    static String safe(String text) {
        return text == null ? "" : text;
    }

    static String fit(String text, int maxWidth) {
        if (visibleLength(text) <= maxWidth) {
            return text;
        }

        StringBuilder fitted = new StringBuilder();
        int visibleCount = 0;

        for (int i = 0; i < text.length() && visibleCount < maxWidth; ) {
            if (text.charAt(i) == '\u001B') {
                int sequenceEnd = findAnsiSequenceEnd(text, i);
                fitted.append(text, i, sequenceEnd);
                i = sequenceEnd;
                continue;
            }

            fitted.append(text.charAt(i));
            i++;
            visibleCount++;
        }

        return fitted.toString();
    }

    static int visibleLength(String text) {
        return safe(text).replaceAll(ANSI_ESCAPE_REGEX, "").length();
    }

    private static String formatHeaderLine(String styledHeaderText, String ansiLineColor) {
        int headerWidth = visibleLength(styledHeaderText);
        String flexRest = ansiLineColor + "─".repeat((INNER_WIDTH - headerWidth) % 2) + DEFAULT_FG;
        String flexLine = ansiLineColor + "─".repeat((INNER_WIDTH - headerWidth) / 2) + DEFAULT_FG;
        return " ".repeat(MARGIN) + flexLine + " " + styledHeaderText + " " + flexLine + flexRest + DEFAULT_FG;
    }

    private static int findAnsiSequenceEnd(String text, int startIndex) {
        int i = startIndex + 1;
        if (i >= text.length() || text.charAt(i) != '[') {
            return Math.min(text.length(), startIndex + 1);
        }

        i++;
        while (i < text.length()) {
            char current = text.charAt(i++);
            if (current >= '@' && current <= '~') {
                break;
            }
        }

        return i;
    }

    public static String colorCurrentValue(Object value) {
        return ANSI.BRIGHT_BLACK + "[" + value + "]" + ANSI.DEFAULT_FG;
    }

    public static String colorHint(String hint) {
        return ANSI.BRIGHT_BLACK + "(" + hint + ")" + ANSI.DEFAULT_FG;
    }

    public static String colorOptions(String options) {
        return ANSI.BRIGHT_BLACK + "(" + options + ")" + ANSI.DEFAULT_FG;
    }

}
