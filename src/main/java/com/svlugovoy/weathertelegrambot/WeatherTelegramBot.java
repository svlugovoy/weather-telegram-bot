package com.svlugovoy.weathertelegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.username}")
    private String username;

    @Value("${telegram.token}")
    private String token;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ParserService parserService;

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId());

            message.setReplyMarkup(getReplyKeyboardMarkup());
            handleInput(update, message);

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(Update update, SendMessage message) {
        switch (update.getMessage().getText()) {
            case "Kyiv":
                try {
                    ResultDto dto = parserService.parse(weatherService.getWeatherByCityNameAndCountryCode("Kyiv", "ua"));
                    message.setText(printResult(dto));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Warsaw":
                try {
                    ResultDto dto = parserService.parse(weatherService.getWeatherByCityNameAndCountryCode("Warsaw", "pl"));
                    message.setText(printResult(dto));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "New York":
                try {
                    ResultDto dto = parserService.parse(weatherService.getWeatherByCityId("5128638"));
                    message.setText(printResult(dto));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "Say something nice!":
                message.setText("You are awesome!!! \uD83D\uDE09 \nHave a nice day!");
                break;
            default:
                message.setText("\uD83D\uDE1E Not supported yet.");
                break;
        }
    }

    private String printResult(ResultDto dto) {
        return LocalDate.now() +
                "\n" + dto.getName() + ", " + dto.getSysCountry() + " - " + dto.getWeatherDescription() +
                "\n" + dto.getMainTemp() + "°С" + ", температура від " + dto.getMinTemp() + " до " + dto.getMaxTemp() + " °С, вітер " + dto.getWindSpeed() + " m/s.";
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Kyiv");
        row.add("Warsaw");
        row.add("New York");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("Say something nice!");
        // Add the second row to the keyboard
        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
