package com.gricko.telegram.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotState {

    Start {
        @Override
        public void enter (BotContext context){
            sendMessage(context,"Hello");
        }

        @Override
        public BotState nextState(){
            return  EnterPhone;
        }
    },

    EnterPhone{
        @Override
        public void enter(BotContext context){
            sendMessage(context,"Enter your phone number please:");
        }


        @Override
        public void handleInput(BotContext context){
            context.getUser().setPhone(context.getInput());
        }


        @Override
        public BotState nextState() {
            return EnterMail;
        }
    },

    EnterMail{
        private BotState next;

        @Override
        public void enter(BotContext context){
            sendMessage(context,"Enter your email address please:");
        }

        @Override
        public void handleInput(BotContext context){
            String email = context.getInput();

            if (Utils.isValidEmail(email)){
                context.getUser().setEmail(context.getInput());
                next = Approved;
            }else{
                sendMessage(context, "Wrong e-mail address!");
                next = EnterMail;
            }
        }

        @Override
        public BotState nextState() {return next;}
    },

    Approved(false){
        @Override
        public void enter(BotContext context){

            sendMessage(context,"Thanks YOU! \n you can use command 'quote'!");
        }

        @Override
        public BotState nextState() {
            return null;
        }

    };

    private static BotState[]states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }
    public static BotState geInitialState(){
        return byId(0);
    }

    public static BotState byId(int id){
        if (states == null){
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context,String text){
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);
        try {
            context.getBot().execute(message);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded(){return inputNeeded;}

    public void handleInput(BotContext context){
        //default method
    }

    public abstract void enter(BotContext context);
    public abstract BotState nextState();



}
