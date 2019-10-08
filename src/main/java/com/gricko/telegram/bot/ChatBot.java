package com.gricko.telegram.bot;

import com.gricko.telegram.model.User;
import com.gricko.telegram.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);

    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";

    private final UserService userService;

    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;


    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();

        User user = userService.findByChatId(chatId);

        if (checkIfAdminCommand(user,text))
            return;

        BotContext context;
        BotState state;

        if (user == null){
            state = BotState.geInitialState();

            user = new User(chatId,state.ordinal());
            userService.addUser(user);

            context = BotContext.of(this,user,text);
            state.enter(context);

            LOGGER.info("New user registered: "+ chatId);
        }else {
            context = BotContext.of(this,user,text);
            state = BotState.byId(user.getStatetId());

            LOGGER.info("Update received for user in state: "+ state);
        }

        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        }while (!state.isInputNeeded());

        user.setStatetId(state.ordinal());
        userService.updateUser(user);
    }
        //Method when User some letter..
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private boolean checkIfAdminCommand(User user, String text){
        if (user == null || !user.getAdmin())
            return false;

        if (text.startsWith(BROADCAST)){
            LOGGER.info("Admin command received: " + BROADCAST);

            text = text.substring(BROADCAST.length());
            broadcast(text);

            return true;
        }else if (text.equals(LIST_USERS)){
            LOGGER.info("Admin command received: " + LIST_USERS);

            listUsers(user);
            return true;
        }
        return false;
    }

    private void sendMessage(long chatId, String text){
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            execute(message);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void listUsers(User admin){
        StringBuilder sb = new StringBuilder("Al    l user list: \r\n");
        List<User> users = userService.findAllUsers();

        users.forEach(user ->
                sb.append(user.getId())
                    .append(' ')
                    .append(user.getPhone())
                    .append(' ')
                    .append(user.getEmail())
                    .append("\r\n")
                );

        sendMessage(admin.getChatId(),sb.toString());
    }

    private void broadcast (String text){
        List<User> users = userService.findAllUsers();
        users.forEach(user -> sendMessage(user.getChatId(),text));
    }
}
