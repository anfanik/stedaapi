package me.anfanik.steda.api.utility;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author Anfanik
 * Date: 18/09/2019
 */

public class TextUtility {

    public static String colorize(String message, Object... arguments) {
        if (message == null) {
            return null;
        }
        if (arguments.length > 0) {
            message = String.format(message, arguments);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(Collection<String> lines) {
        List<String> coloredLines = new ArrayList<>();
        for (String line : lines) {
            coloredLines.add(colorize(line));
        }
        return coloredLines;
    }

    public static String uncolorize(String message) {
        return message == null ? null : ChatColor.stripColor(colorize(message));
    }

    public static List<String> uncolorize(Collection<String> lines) {
        List<String> uncoloredLines = new ArrayList<>();
        for (String line : lines) {
            uncoloredLines.add(uncolorize(line));
        }
        return uncoloredLines;
    }

    public static String mergeLines(String... lines) {
        return mergeLines(Arrays.asList(lines));
    }

    public static String mergeLines(Collection<String> lines) {
        StringBuilder builder = new StringBuilder();
        boolean firstLine = true;
        for (String line : lines) {
            if (line.isEmpty()) {
                line = " ";
            }
            if (!firstLine) {
                builder.append("\n");
            } else {
                firstLine = false;
            }
            builder.append(colorize("&r")).append(colorize(line));
        }
        return builder.toString();
    }

    public static String formatDouble(double value) {
        return new DecimalFormat("0.0").format(value)
                .replace(',', '.');
    }

    public static String formatTimestamp(long timestamp) {
        int seconds = (int) (timestamp / 1000);

        int minutes = seconds / 60;
        if (minutes > 0) {
            seconds -= minutes * 60;
        }

        int hours = minutes / 60;
        if (hours > 0) {
            minutes -= hours * 60;
        }

        int days = hours / 24;
        if (days > 0) {
            hours -= days * 24;
        }

        int weeks = days / 7;
        if (weeks > 0) {
            days -= weeks * 7;
        }

        int months = weeks / 4;
        if (months > 0) {
            weeks -= months * 4;
        }

        TimeRegister register;
        int registerValue;
        if (months > 0) {
            register = TimeRegister.MONTH;
            registerValue = months;
        } else if (weeks > 0) {
            register = TimeRegister.WEEK;
            registerValue = weeks;
        } else if (days > 0) {
            register = TimeRegister.DAY;
            registerValue = days;
        } else if (hours > 0) {
            register = TimeRegister.HOUR;
            registerValue = hours;
        } else if (minutes > 0) {
            register = TimeRegister.MINUTE;
            registerValue = minutes;
        } else {
            register = TimeRegister.SECOND;
            registerValue = seconds;
        }

        StringBuilder builder = new StringBuilder();
        if (register != TimeRegister.SECOND) {
            builder.append("около ");
        }
        builder.append(String.format("%d %s", registerValue, register.transformViaNumeral(registerValue)));

        for (TimeRegister timeRegister : register.getIncluding()) {
            int timeRegisterValue = 0;

            switch (timeRegister) {
                case MONTH: timeRegisterValue = months; break;
                case WEEK: timeRegisterValue = weeks; break;
                case DAY: timeRegisterValue = days; break;
                case HOUR: timeRegisterValue = hours; break;
                case MINUTE: timeRegisterValue = minutes; break;
                case SECOND: timeRegisterValue = seconds; break;
            }

            builder.append(" ").append(String.format("%d %s", timeRegisterValue, timeRegister.transformViaNumeral(timeRegisterValue)));
        }

        return builder.toString();
    }

    private enum TimeRegister {
        SECOND(amount -> transformViaAmount(amount, "секунда", "секунды", "секунд")), //1 секунда
        MINUTE(amount -> transformViaAmount(amount, "минуты", "минут", "минут")), //1 минута
        HOUR(amount -> transformViaAmount(amount, "часа", "часов", "часов"), MINUTE), //1 час 1 минута
        DAY(amount -> transformViaAmount(amount, "дня", "дней", "дней"), HOUR), //1 день 1 час
        WEEK(amount -> transformViaAmount(amount, "недели", "недель", "недель"), DAY), //1 неделя 1 день
        MONTH(amount -> transformViaAmount(amount, "месяца", "месяцев", "месяцев"), WEEK); //1 месяц 1 неделя

        private final Function<Integer, String> numeralTransformationFunction;

        @Getter
        private final TimeRegister[] including;

        TimeRegister(Function<Integer, String> numeralTransformationFunction, TimeRegister... including) {
            this.numeralTransformationFunction = numeralTransformationFunction;
            this.including = including;
        }

        public String transformViaNumeral(int count) {
            return numeralTransformationFunction.apply(count);
        }
    }

    public static String transformViaAmount(int amount, String one, String two, String many) {
        int mod10 = amount % 10, mod100 = amount % 100;
        if (mod10 == 1 && mod100 != 11) {
            return one;
        }
        if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 > 20)) {
            return two;
        }
        return many;
    }

    private static final int CHAT_SIZE = 74;

    public static String align(String message) {
        return getAlignSpaces(message) + colorize(message);
    }

    public static String getAlignSpaces(String message) {
        int spaces = getAlignSpacesAmount(message);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; ++i) {
            builder.append(" ");
        }

        return builder.toString();
    }

    public static int getAlignSpacesAmount(String message) {
        int length = uncolorize(message).length();
        return (CHAT_SIZE - length) / 2;
    }

    private TextUtility() {
    }

}
