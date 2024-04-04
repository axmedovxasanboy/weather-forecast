package uz.weather.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.weather.bot.bot.Bot;

import java.util.List;

public class CallbackHandler implements BaseHandler {
    private final SendMessage message = new SendMessage();
    @Override
    public void handle(Update update, Bot bot) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getFrom().getId();
        message.setChatId(chatId);

        sendMenu(chatId, callbackQuery, message);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    private void sendMenu(Long chatId, CallbackQuery callbackQuery, SendMessage message) {
        String data = callbackQuery.getData();

        if(data.equals("startForecast")){
            message.setText("Please enter city name");
            locationKeyboard(message);

        }

    }

    private void locationKeyboard(SendMessage message) {
        ReplyKeyboardMarkup keyboard = getLocationKeyboard();

        message.setReplyMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getLocationKeyboard() {
        KeyboardButton btn1 = new KeyboardButton();
        KeyboardRow row1 = new KeyboardRow();
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        row1.add(btn1);

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setKeyboard(List.of(row1));
        return keyboard;
    }
}
