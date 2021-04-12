package by.tg.scheduller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            telegramBotsApi.registerBot(new Bot());
        }catch (TelegramApiException e){
            System.out.println(e);
        }
    }

    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch (message.getText()){
                case "/start" :sendMsg(message);
                break;
                case "/help": sendMsg(message);
                break;
                case "96": sendMsg(message);
                    break;
                case "132" : sendMsg(message);
                    break;
            }
        }

    }

    public void sendMsg(Message message) {
        String time = null;
        try {
            time = process(message.getText());
        } catch (IOException e) {
            System.out.println("sorry");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(time);
        try {
            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(new KeyboardButton("96"));
        keyboardRow.add(new KeyboardButton("132"));

        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "MyRoadSchedullerBot";
    }

    public String getBotToken() {
        return "1244948527:AAGQEzOnsHItXxs7pBTlq-1Dxr1-Hz9HLsw";
    }

    public String process(String road) throws IOException {

        Document document = null;

        StringBuilder result = new StringBuilder("Слудеющий в ");

        switch(road){
            case "96": document = Jsoup.connect("https://kogda.by/routes/minsk/autobus/96/%D0%94%D0%A1%20%D0%9C%D0%B0%D0%BB%D0%B8%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0-4%20-%20%D0%A4%D0%B8%D0%BB%D0%B8%D0%B0%D0%BB%20%D0%91%D0%93%D0%A3/%D0%90%D0%BA%D0%B0%D0%B4%D0%B5%D0%BC%D0%B8%D0%BA%D0%B0%20%D0%9A%D1%83%D1%80%D1%87%D0%B0%D1%82%D0%BE%D0%B2%D0%B0").get();
            break;
            case "132": document = Jsoup.connect("https://kogda.by/routes/minsk/autobus/132/%D0%94%D0%A1%20%D0%9C%D0%B0%D0%BB%D0%B8%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0-4%20-%20%D0%A9%D0%BE%D0%BC%D1%8B%D1%81%D0%BB%D0%B8%D1%86%D0%B0/%D0%90%D0%BA%D0%B0%D0%B4%D0%B5%D0%BC%D0%B8%D0%BA%D0%B0%20%D0%9A%D1%83%D1%80%D1%87%D0%B0%D1%82%D0%BE%D0%B2%D0%B0").get();
            break;
        }

        if(document != null){
            Elements elements = document.getElementsByClass("future");
            String str = elements.get(0).toString();
            char ch[] = str.toCharArray();
            for(int i = 0; i < ch.length;i++) {
                if( (int) ch[i] >= 48 && (int) ch[i] <= 57 || (int) ch[i] == 58){
                    result.append(ch[i]);
                }
            }

            result.append(" и в ");

            String str2 = elements.get(1).toString();
            char ch2[] = str2.toCharArray();
            for (char c : ch2) {
                if ((int) c >= 48 && (int) c <= 57 || (int) c == 58) {
                    result.append(c);
                }
            }

        }else {
            return "Privet";
        }



        return result.toString();

    }

}
