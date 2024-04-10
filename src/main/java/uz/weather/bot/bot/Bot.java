package uz.weather.bot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.handlers.BaseHandler;
import uz.weather.bot.handlers.CallbackHandler;
import uz.weather.bot.handlers.MessageHandler;

public class Bot extends TelegramLongPollingBot {

    private final BaseHandler messageHandler;
    private final BaseHandler callbackHandler;

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            messageHandler.handle(update, chatId, this);
        }else{
            chatId = update.getCallbackQuery().getFrom().getId();
            callbackHandler.handle(update, chatId, this);
        }
    }

    public Bot() {
        messageHandler = new MessageHandler();
        callbackHandler = new CallbackHandler();
    }

    @Override
    public String getBotUsername() {
        return Props.get("bot.username", "resources/bot.properties");
    }

    @Override
    public String getBotToken() {
        return Props.get("bot.token", "resources/bot.properties");
    }
}
