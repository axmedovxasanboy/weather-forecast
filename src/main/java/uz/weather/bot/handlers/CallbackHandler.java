package uz.weather.bot.handlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.weather.Instances;
import uz.weather.bot.bot.Bot;
import uz.weather.bot.bot.Steps;
import uz.weather.model.API;
import uz.weather.model.main.DailyForecasts;
import uz.weather.model.main.HourlyForecasts;
import uz.weather.model.main.WeatherForecast;
import uz.weather.model.search.Search;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CallbackHandler implements BaseHandler {
    private final WeatherForecast forecast = Instances.forecast;
    private Integer cityKey;
    private String fullCityName;
    private final HashMap<Long, List<DailyForecasts>> allDailyForecast = new HashMap<>();
    private final HashMap<Long, List<HourlyForecasts>> allHourlyForecast = new HashMap<>();
    private Integer day;


    @Override
    public void handle(Update update, Long chatId, Bot bot) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
        if (data.endsWith("hour"))
            Steps.set(chatId, "hourly");

        else if (data.endsWith("day"))
            Steps.set(chatId, "daily");

        else if (Steps.get(chatId).equals("detailed_selected"))
            Steps.set(chatId, "daily");

        String step = Steps.get(chatId);
        switch (step) {
            case "city searched", "city selected" -> {
                if (data.startsWith("city_key_")) {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.enableHtml(true);
                    cityKey = Integer.parseInt(data.split("_")[2]);
                    allDailyForecast.put(chatId, (List<DailyForecasts>) forecast.sendForecasts(cityKey, true));
                    List<DailyForecasts> dailyForecasts = allDailyForecast.getOrDefault(chatId, null);
                    if (dailyForecasts == null) {
                        allDailyForecast.put(chatId, (List<DailyForecasts>) forecast.sendForecasts(cityKey, true));
                        dailyForecasts = allDailyForecast.getOrDefault(chatId, null);
                    }
                    Steps.set(chatId, "city selected");
                    sendSearched(cityKey, message, dailyForecasts, chatId);
                    execute(bot, message);
                }
            }
            case "hourly" -> {
                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.enableHtml(true);
                message.setMessageId(callbackQuery.getMessage().getMessageId());
                allHourlyForecast.put(chatId, (List<HourlyForecasts>) forecast.sendForecasts(cityKey, false));
                List<HourlyForecasts> hourlyForecasts = allHourlyForecast.getOrDefault(chatId, null);
                if (hourlyForecasts == null) {
                    allHourlyForecast.put(chatId, (List<HourlyForecasts>) forecast.sendForecasts(cityKey, false));
                    hourlyForecasts = allHourlyForecast.getOrDefault(chatId, null);
                }
                if (data.equals("one_hour")) {
                    getTypesOfForecast(message, API.ONE_HOUR, chatId);
                    message.setText(setTextForHourly(hourlyForecasts, 0));
                    execute(bot, message);
                } else if (data.equals("twelve_hour")) {
                    getTypesOfForecast(message, API.TWELVE_HOUR, chatId);
                    message.setText(setTextForHourly(hourlyForecasts, 0));
                    execute(bot, message);
                } else if (data.endsWith("hour_forecast")) {
                    getTypesOfForecast(message, API.TWELVE_HOUR, chatId);
                    Integer order = Integer.parseInt(data.split("_")[0]);
                    message.setText(setTextForHourly(hourlyForecasts, order));
                    execute(bot, message);
                }

            }
            case "daily" -> {
                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.enableHtml(true);
                message.setMessageId(callbackQuery.getMessage().getMessageId());
                List<DailyForecasts> dailyForecasts = allDailyForecast.getOrDefault(chatId, null);
                if (data.equals("one_day")) {
                    day = 0;
                    getTypesOfForecast(message, API.ONE_DAY, chatId);
                    message.setText(setTextForDaily(dailyForecasts, 0));
                    execute(bot, message);
                } else if (data.equals("five_day")) {
                    day = 0;
                    getTypesOfForecast(message, API.FIVE_DAY, chatId);
                    message.setText(setTextForDaily(dailyForecasts, 0));
                    execute(bot, message);
                } else if (data.endsWith("day_forecast")) {
                    Integer order = Integer.parseInt(data.split("_")[0]);
                    day = order;
                    getTypesOfForecast(message, API.FIVE_DAY, chatId);
                    message.setText(setTextForDaily(dailyForecasts, order));
                    execute(bot, message);
                } else if (data.endsWith("_detailed_forecast_day")) {
                    Integer order = Integer.parseInt(data.split("_")[0]);
                    Steps.set(chatId, "detailed_selected");
                    getTypesOfForecast(message, API.FIVE_DAY, chatId);
                    message.setText(setTextForDailyDetailed(dailyForecasts, order));
                    execute(bot, message);
                }
            }
        }

    }

    private void execute(Bot bot, EditMessageText message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void execute(TelegramLongPollingBot bot, SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void sendSearched(Integer cityKey, SendMessage message, List<DailyForecasts> dailyForecasts, Long chatId) {
        fullCityName = getFullCityName(cityKey, forecast.getSearches());
        day = 0;
        getTypesOfForecast(message, chatId);
        if (fullCityName != null) {
            message.setText(setTextForDaily(dailyForecasts, 0));
        } else {
            System.out.println("forecast.getSearches() is returning null");
        }
    }

    private void getTypesOfForecast(SendMessage message, Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboardMarkup(API.ONE_DAY, chatId);
        message.setReplyMarkup(keyboardMarkup);

    }

    private void getTypesOfForecast(EditMessageText message, API nonProviding, Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = getInlineKeyboardMarkup(nonProviding, chatId);
        message.setReplyMarkup(keyboardMarkup);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(API nonProviding, Long chatId) {
        API[] values = API.values();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> column = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        String buttonText = String.valueOf(nonProviding);
        String split1 = buttonText.split("_")[0];
        switch (split1) {
            case "TWELVE" -> setCallbackQuery(row, column, 12, chatId);
            case "FIVE" -> setCallbackQuery(row, column, 5, chatId);
        }
        row = new ArrayList<>();


        for (API value : values) {
            if (value == API.SEARCH || value == nonProviding) continue;
            String[] split = String.valueOf(value).split("_");
            switch (split[0]) {
                case "ONE" -> {
                    if (split[1].equals("DAY")) buttonText = "1 day";
                    else if (split[1].equals("HOUR")) buttonText = "1 hour";
                }
                case "TWELVE" -> buttonText = "12 hour";
                case "FIVE" -> buttonText = "5 day";
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonText);
            String expression = buttonText.split(" ")[0];
            String holder = expression.equals("1") ? "one" : expression.equals("5") ? "five" : "twelve";

            button.setCallbackData(holder + "_" + buttonText.split(" ")[1]);
            row.add(button);
        }
        column.add(row);
        if (String.valueOf(nonProviding).toLowerCase().endsWith("day") && !Steps.get(chatId).equals("detailed_selected")) {
            row = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText("Detailed forecast");
            btn.setCallbackData(day + "_detailed_forecast_day");
            row.add(btn);
            column.add(row);
            Steps.set(chatId, "daily");
        }
        keyboardMarkup.setKeyboard(column);
        return keyboardMarkup;
    }


    private void setCallbackQuery(List<InlineKeyboardButton> row, List<List<InlineKeyboardButton>> column, Integer number, Long chatId) {
        String helper = number == 12 ? "hour" : "day", helper2;
        List<DailyForecasts> dailyForecasts = allDailyForecast.get(chatId);
        List<HourlyForecasts> hourlyForecasts = allHourlyForecast.get(chatId);

        for (int i = 1; i <= number; i++) {
            if (helper.equals("hour")) helper2 = hourlyForecasts.get(i - 1).getDateTime().split("T")[1].substring(0, 5);
            else {
                DailyForecasts daily = dailyForecasts.get(i - 1);
                helper2 = getDate(daily.getDate().substring(0, daily.getDate().indexOf("T")));
            }
            InlineKeyboardButton b1 = new InlineKeyboardButton();
            b1.setText(helper2);
            b1.setCallbackData((i - 1) + "_" + helper + "_forecast");
            row.add(b1);
            if (number == 5 && i == 3) {
                column.add(row);
                row = new ArrayList<>();
            }

            if (i % 4 == 0 && number == 12) {
                column.add(row);
                row = new ArrayList<>();
            }
        }
        column.add(row);

    }

    private String getFullCityName(Integer cityKey, List<Search> searches) {
        StringBuilder fullName = new StringBuilder();
        for (Search search : searches) {
            if (search.getKey().equals(cityKey)) {
                fullName.append("<b>").append(search.getEnglishName()).append(" ").append(search.getAdministrativeArea().getEnglishType()).append(", ").
                        append(search.getCountry().getEnglishName()).append("</b>\n");
                return String.valueOf(fullName);
            }
        }
        return null;
    }

    private String setTextForHourly(List<HourlyForecasts> hourlyForecasts, Integer order) {
        HourlyForecasts hourly = hourlyForecasts.get(order);
        StringBuilder builder = new StringBuilder();
        String date = getDate(hourly.getDateTime().split("T")[0]);
        String dateTime = hourly.getDateTime().split("T")[1];
        String hour = dateTime.substring(0, 5);
        StringBuilder helper = new StringBuilder();
        if (hourly.getHasPrecipitation()) {
            helper.append("✅ Precipitation (with chance of ").append(hourly.getPrecipitationProbability()).append(" %): ").
                    append(hourly.getPrecipitationIntensity()).append(" ").append(hourly.getPrecipitationType().toLowerCase()).append("\n");
        }

        Number maxTemp = FtoC(hourly.getTemperature().getValue());
        Number realFeelTemp = FtoC(hourly.getRealFeelTemperature().getValue());

        builder.append(fullCityName).append("\uD83D\uDCC5 ").append(date).append("\n")
                .append("\uD83D\uDD52 ").append(hour).append(" (GMT").append(dateTime, dateTime.length() - 6, dateTime.length() - 3).append(")").append("\n")
                .append(hourly.getHasPrecipitation() ? helper : "\uD83D\uDEAB No any kind of precipitation\n")
                .append("Temperature: ").append(maxTemp).append("C° (").append(hourly.getTemperature().getValue()).append("F°)\n")
                .append("\uD83D\uDEB6\uD83C\uDFFB\u200D♂️ Outside is ").append(hourly.getRealFeelTemperature().getPhrase().toLowerCase()).append("\n")
                .append("Feels like ").append(realFeelTemp).append("C° (").append(hourly.getRealFeelTemperature().getValue()).append("F°)\n")
                .append("\uD83D\uDCA8 Wind speed: ").append(mileToKm(hourly.getWind().getSpeed().getValue())).append(" km/h (").append(hourly.getWind().getSpeed().getValue()).append(" mi/h)");

        return builder.toString();
    }

    private String setTextForDaily(List<DailyForecasts> dailyForecasts, Integer order) {
        StringBuilder builder = new StringBuilder();
        DailyForecasts daily = dailyForecasts.get(order);
        String date = getDate(daily.getDate().substring(0, daily.getDate().indexOf("T")));

        Number minTempC = FtoC(daily.getTemperature().getMinimum().getValue());
        Number maxTempC = FtoC(daily.getTemperature().getMaximum().getValue());
        Number daySpeedK = mileToKm(daily.getDay().getWind().getSpeed().getValue());
        Number nightSpeedK = mileToKm(daily.getNight().getWind().getSpeed().getValue());

        builder.append(fullCityName).append(date).append("\n\n").
                append("\uD83C\uDF04 Day: ").append(daily.getDay().getShortPhrase()).append(".\n").
                append("High: ").append(maxTempC).append("°C (").append(daily.getTemperature().getMaximum().getValue()).append("°F)\n").
                append("\uD83D\uDCA8 Wind speed: ").append(daySpeedK).append(" km/h (").append(daily.getDay().getWind().getSpeed().getValue()).append("mi/h)\n\n").
                append("\uD83C\uDF19 Night: ").append(daily.getNight().getShortPhrase()).append(".\n").
                append("Low: ").append(minTempC).append("°C (").append(daily.getTemperature().getMinimum().getValue()).append("°F)\n").
                append("\uD83D\uDCA8 Wind speed: ").append(nightSpeedK).append(" km/h (").append(daily.getNight().getWind().getSpeed().getValue()).append("mi/h)");

        return builder.toString();
    }

    private String setTextForDailyDetailed(List<DailyForecasts> dailyForecasts, Integer order) {
        StringBuilder builder = new StringBuilder();
        StringBuilder dayPrecipitation = new StringBuilder();
        StringBuilder nightPrecipitation = new StringBuilder();
        DailyForecasts daily = dailyForecasts.get(order);
        String date = getDate(daily.getDate().substring(0, daily.getDate().indexOf("T")));
        if (daily.getDay().getHasPrecipitation()) {
            dayPrecipitation.append("✅ Precipitation (with chance of ").append(daily.getDay().getPrecipitationProbability()).append(" %) : ").
                    append(daily.getDay().getPrecipitationIntensity()).append(" ").
                    append(daily.getDay().getPrecipitationType().toLowerCase()).append("\n");
        }
        if (daily.getNight().getHasPrecipitation()) {
            nightPrecipitation.append("✅ Precipitation (with chance of ").append(daily.getDay().getPrecipitationProbability()).append(" %): ").
                    append(daily.getDay().getPrecipitationIntensity()).append(" ").
                    append(daily.getDay().getPrecipitationType().toLowerCase()).append("\n");
        }
        Number minTempC = FtoC(daily.getTemperature().getMinimum().getValue());
        Number maxTempC = FtoC(daily.getTemperature().getMaximum().getValue());
        Number maxRealTempC = FtoC(daily.getRealFeelTemperature().getMaximum().getValue());
        Number minRealTempC = FtoC(daily.getRealFeelTemperature().getMinimum().getValue());
        Number daySpeedK = mileToKm(daily.getDay().getWind().getSpeed().getValue());
        Number nightSpeedK = mileToKm(daily.getNight().getWind().getSpeed().getValue());


        builder.append(fullCityName).append(date).append("\n\n").
                append("\uD83C\uDF05 Sunrise: ").append(daily.getSun().getRise().split("T")[1], 0, 5).append("\n").
                append("\uD83C\uDF06 Sunset: ").append(daily.getSun().getSet().split("T")[1], 0, 5).append("\n\n").
                append("\uD83C\uDF04 Day: ").append(daily.getDay().getLongPhrase()).append(".\n").
                append(daily.getDay().getHasPrecipitation() ? dayPrecipitation : "\uD83D\uDEAB No any kind of precipitation on day light\n").
                append("High: ").append(maxTempC).append("°C (").append(daily.getTemperature().getMaximum().getValue()).append("°F)\n").
                append("Feels like ").append(maxRealTempC).append("C° (").append(daily.getRealFeelTemperature().getMaximum().getValue()).append("F°)\n").
                append("\uD83D\uDEB6\uD83C\uDFFB\u200D♂️ It is ").append(daily.getRealFeelTemperature().getMaximum().getPhrase().toLowerCase()).append(" on day light\n").
                append("\uD83D\uDCA8 Wind speed: ").append(daySpeedK).append(" km/h (").append(daily.getDay().getWind().getSpeed().getValue()).append("mi/h)\n\n").
                append("\uD83C\uDF19 Night: ").append(daily.getNight().getLongPhrase()).append(".\n").
                append(daily.getNight().getHasPrecipitation() ? nightPrecipitation : "\uD83D\uDEAB No any kind of precipitation at night\n").
                append("Low: ").append(minTempC).append("°C (").append(daily.getTemperature().getMinimum().getValue()).append("°F)\n").
                append("Feels like ").append(minRealTempC).append("C° (").append(daily.getRealFeelTemperature().getMinimum().getValue()).append("F°)\n").
                append("\uD83D\uDEB6\uD83C\uDFFB\u200D♂️ It is ").append(daily.getRealFeelTemperature().getMinimum().getPhrase().toLowerCase()).append(" at night\n").
                append("\uD83D\uDCA8 Wind speed: ").append(nightSpeedK).append(" km/h (").append(daily.getNight().getWind().getSpeed().getValue()).append("mi/h)");

        return builder.toString();
    }

    private Number mileToKm(Double daySpeedM) {
        return Math.round(daySpeedM * (1.609344));
    }

    private Number FtoC(Double fahrenheit) {
        return Math.round((fahrenheit - 32) * 5 / 9);
    }

    private static String getDate(String dailyDate) {
        LocalDate localDate = LocalDate.parse(dailyDate);
        localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        dailyDate = localDate.getDayOfMonth() + "-" + localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + localDate.getYear();
        return dailyDate;
    }
}
