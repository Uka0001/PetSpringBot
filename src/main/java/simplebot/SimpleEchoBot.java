package simplebot;
/*
* Creating the main class of simple echo bot
* */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/*
* Main class extends Telegram Long Polling Bot from telegram library
* */
public class SimpleEchoBot extends TelegramLongPollingBot {

    /*inject my bot token into the application by using Environment Variables.
    Environment Variables are Values that are set in the Environment the Bot is running.*/
    String botToken = System.getenv("VARIABLE_TOKEN");
    String botUsername = System.getenv("VARIABLE_NAME");

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /*
    * Implementing three methods from father class TelegramLongPollingBot
    * */

    /*wright bot username that we have created from BotFather in Telegram*/
    @Override
    public String getBotUsername() {
        // username which you give to your bot via BotFather (without @)
        return botUsername;
    }

    @Override
    public String getBotToken() {
        // do not expose the token to the repository,
        // always provide it externally(for example as environmental variable)
        return botToken;
    }

    /*
     * This is an entry point for any messages, or updates received from user<br>
     * Docs for "Update object: https://core.telegram.org/bots/api#update
     **/
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();

            Long userId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            //printing user messages
            log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);

            //Building Request to be sent to Telegram API
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChat().getId().toString())
                    .text("Hello, I've received your text: " + textFromUser)
                    .build();
            try {
                //sending message via Telegram API
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Exception when sending message: ", e);
            }
        } else {
            log.warn("Unexpected update from user");
        }
    }
}
