package uz.weather.bot.handlers;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.weather.bot.bot.Bot;
import uz.weather.bot.bot.Steps;
import uz.weather.model.forecast.WeatherForecast;
import uz.weather.model.search.Search;

import java.util.ArrayList;
import java.util.List;


public class MessageHandler implements BaseHandler {
    protected SendMessage message = new SendMessage();
    protected TelegramLongPollingBot bot;


    @Override
    public void handle(Update update, Bot bot) {
        this.bot = bot;

        Location location;

        Message userMsg = update.getMessage();

        Long chatId = userMsg.getChatId();

        String userText = userMsg.getText();

        message.setChatId(chatId);

        Contact contact = Steps.getContact(chatId);

        if (userMsg.hasContact()) {
            Steps.setContact(chatId, userMsg.getContact());
            contact = userMsg.getContact();
            Steps.set(chatId, "Contact accepted");
            System.out.println(contact);
        }

        if (userMsg.hasLocation()) {
            location = userMsg.getLocation();
            System.out.println(location.getLatitude());
            System.out.println(location.getLongitude());
        }

        sendMenu(chatId, contact, userMsg, message);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void sendMenu(Long chatId, Contact contact, Message userMsg, SendMessage message) {
        String text = userMsg.getText();
        String step = Steps.get(chatId);

        if (step.equals("main")) {
            if (text.equals("/start")) {
                contactKeyboard(message);
                message.setText("Welcome to Weather Forecast Telegram bot. Please share your contact to use our bot fully.");
            }
        } else if (step.equals("Contact accepted")) {
            handleContact(chatId, contact, message);
        } else if (step.equals("city")) {
            handleSearch(chatId, message, userMsg);
        }

    }


    private void handleSearch(Long chatId, SendMessage message, Message userMessage) {
        WeatherForecast forecast = new WeatherForecast();
        String cityName = userMessage.getText();
        List<Search> searchResponse = forecast.searchCity(cityName);
        if (searchResponse == null) {
            message.setText("Unable to find information about entered city\nPlease check again whether city name is correctly written");
        } else if (searchResponse.size() == 1) {
            Search search = searchResponse.getFirst();
            Integer key = search.getKey();

            message.setText(String.valueOf(searchResponse.size()));
        } else {
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Search city : searchResponse) {
                if (i == 10) break;
                builder.append((i + 1)).append(". ").
                        append(city.getEnglishName()).append(", ").
                        append(city.getAdministrativeArea().getEnglishName()).append(", ").
                        append(city.getCountry().getEnglishName()).append('\n');
                i++;
            }
            int size = Math.min(searchResponse.size(), 10);
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            int counter = 0;

            List<InlineKeyboardButton> row = new ArrayList<>();
            for (i = 0; i < size; i++) {
                if (counter == 5) {
                    keyboard.add(row);
                    row = new ArrayList<>();
                    counter = 0;
                }

                InlineKeyboardButton b1 = new InlineKeyboardButton();
                b1.setText(String.valueOf(i + 1));
                b1.setCallbackData(String.valueOf(i));
                row.add(b1);
                counter++;
            }
            if (size % 5 != 0) {
                keyboard.add(row);
            }

            keyboardMarkup.setKeyboard(keyboard);

            message.setReplyMarkup(keyboardMarkup);
            message.setText(String.valueOf(builder));
            Steps.set(chatId, "city found");

        }


    }

    private void handleContact(Long chatId, Contact contact, SendMessage message) {
        String name = contact.getFirstName();
        message.setText("Contact has been accepted, " + name);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        message.setText("Enter city name");
        Steps.set(chatId, "city");
    }

    private void contactKeyboard(SendMessage message) {
        ReplyKeyboardMarkup keyboard = getContactKeyboard();

        message.setReplyMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getContactKeyboard() {
        KeyboardButton btn1 = new KeyboardButton();
        KeyboardRow row1 = new KeyboardRow();
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        btn1.setRequestContact(true);
        btn1.setText("\uD83D\uDCDE Share");
        row1.add(btn1);

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(List.of(row1));
        return keyboard;
    }

        /*
        TODO: Azizbek
          Telegram botni (username va token ni bot.properties filedan topasiz)
          user 1- marta ishga tushirganda, (Application file orqali ishga tushirilsin)
          "Welcome to Weather Forecast Telegram bot" matni bilan ishga tushsin
          Userdan contact soralsin va "Share" button chiqsin.
          Contact qabul qilingandan keyin 2 ta button chiqsin: Search va Location
          Search button bosilganda shahar nomi kiritlishi soralsin
          Location button bosilganda location jonatilsin
         */

}
