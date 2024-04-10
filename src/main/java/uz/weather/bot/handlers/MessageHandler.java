package uz.weather.bot.handlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.weather.Instances;
import uz.weather.bot.bot.Bot;
import uz.weather.bot.bot.Steps;
import uz.weather.model.main.WeatherForecast;
import uz.weather.model.search.Search;

import java.util.ArrayList;
import java.util.List;


public class MessageHandler implements BaseHandler {
    protected TelegramLongPollingBot bot;
    protected WeatherForecast forecast = Instances.forecast;


    @Override
    public void handle(Update update, Long chatId, Bot bot) {
        this.bot = bot;

        Message userMsg = update.getMessage();

        Contact contact = Steps.getContact(chatId);

        if (userMsg.getText() != null && userMsg.getText().equals("/start")) {
            contact = Steps.getContact(chatId);
            if (contact != null) {
                Steps.set(chatId, "Contact accepted");
            } else {
                Steps.set(chatId, "main");
            }
        }

        if (userMsg.getText() != null && userMsg.getText().endsWith("Search city")) Steps.set(chatId, "Search city");

        if (userMsg.hasContact()) {
            Steps.setContact(chatId, userMsg.getContact());
            contact = userMsg.getContact();
            Steps.set(chatId, "Contact accepted");
            System.out.println(contact);
        }

        String step = null;
        try {
            step = Steps.get(chatId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (step != null && !step.equals("city searched")) sendMenu(chatId, contact, userMsg);
        else {
            SendMessage message = new SendMessage();
            keyboard(message, "\uD83D\uDD0D Search city", false);
            message.setText("Please use button below to search another city");
        }
    }

    private void sendMenu(Long chatId, Contact contact, Message userMsg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String step = Steps.get(chatId);

        switch (step) {
            case "main" -> {
                keyboard(message, "\uD83D\uDCDE Share", true);
                message.setText("Welcome to Weather Forecast Telegram bot. Please share your contact to use our bot fully.");
            }
            case "Contact accepted" -> handleContact(chatId, contact, message);
            case "Search city" -> {
                keyboard(message, "\uD83D\uDD0D Search city", false);
                message.setText("Enter city name");
                Steps.set(chatId, "search");
            }
            case "search" -> handleSearch(chatId, message, userMsg);
        }
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }


    private void handleSearch(Long chatId, SendMessage message, Message userMessage) {
        String cityName = userMessage.getText();
        List<Search> searchResponse = forecast.searchList(cityName);
        if (searchResponse == null || searchResponse.isEmpty()) {
            message.setText("Unable to find information about entered city\nPlease try to search city names and also check again whether city name is correctly written");
        } else {
            Steps.set(chatId, "city searched");
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            String builder = searchInfo(searchResponse);
            keyboardMarkup.setKeyboard(getInlineKeyboard(searchResponse));
            message.setReplyMarkup(keyboardMarkup);
            message.enableHtml(true);
            message.setText(builder);
        }

    }

    private static List<List<InlineKeyboardButton>> getInlineKeyboard(List<Search> searchResponse) {
        int size = Math.min(searchResponse.size(), 10);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int counter = 0;

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (counter == 5) {
                keyboard.add(row);
                row = new ArrayList<>();
                counter = 0;
            }

            InlineKeyboardButton b1 = new InlineKeyboardButton();
            b1.setText(String.valueOf(i + 1));
            b1.setCallbackData("city_key_" + searchResponse.get(i).getKey());
            row.add(b1);
            counter++;
        }
        if (counter > 0 && counter <= 5) {
            keyboard.add(row);
        }
        return keyboard;
    }

    private static String searchInfo(List<Search> searchResponse) {
        StringBuilder builder = new StringBuilder();
        if (searchResponse.size() > 1) {
            builder.append("<b>Results are from 1-").append(Math.min(searchResponse.size(), 10)).append("</b>\n\n");
        } else {
            builder.append("<b>Only 1 result found</b>\n\n");
        }
        int i = 0;
        for (Search city : searchResponse) {
            if (i == 10) break;
            builder.append((i + 1)).append(". ").
                    append(city.getEnglishName()).append(", ").
                    append(city.getAdministrativeArea().getEnglishName()).append(", ").
                    append(city.getCountry().getEnglishName()).append('\n');
            i++;
        }
        return builder.toString();
    }

    private void handleContact(Long chatId, Contact contact, SendMessage message) {
        String name = contact.getFirstName();
        message.setText("Contact has been accepted, " + name);
        message.enableMarkdown(true);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        keyboard(message, "\uD83D\uDD0D Search city", false);
        message.setText("Enter city name");
        Steps.set(chatId, "search");
    }

    private void keyboard(SendMessage message, String text, Boolean requestContact) {
        KeyboardButton btn1 = new KeyboardButton();
        KeyboardRow row1 = new KeyboardRow();
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        btn1.setRequestContact(requestContact);
        btn1.setText(text);
        row1.add(btn1);

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(List.of(row1));

        message.setReplyMarkup(keyboard);
    }

}
