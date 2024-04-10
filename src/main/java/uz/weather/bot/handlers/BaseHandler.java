package uz.weather.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.bot.Bot;

public interface BaseHandler {
    void handle(Update update, Long chatId, Bot bot);
}
