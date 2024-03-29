package uz.weather.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.bot.Bot;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler implements BaseHandler {
    protected SendMessage message = new SendMessage();
    private Map<Long, UserState> userStates = new HashMap<>();
    private Long chatId;
    private enum UserState {
        AWAITING_CONTACT,
        AWAITING_CITY_NAME,
        AWAITING_LOCATION
    }
    @Override
    public void handle(Update update, Bot bot) {
        Message userMsg = update.getMessage();

        String userText = userMsg.getText();

        chatId = userMsg.getChatId();

        message.setChatId(chatId);

        if (userText.equals("/start")) {
            message.setText("Welcome to Weather Forecast Telegram bot");
        } else if (userMsg.hasContact()) {
            handleContact(userMsg.getContact());
        } else if (userText.equals("Search")) {
            handleSearch();
        } else if (userText.equals("Location")) {
            handleLocation();
        }
    }

    private void handleLocation() {
    }

    private void handleSearch() {
        
    }

    private void handleContact(Contact contact) {
        
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
