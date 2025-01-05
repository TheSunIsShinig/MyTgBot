package org.example.bot;

import org.example.controller.ResponseHandler;
import org.example.service.AbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

import static org.example.constant.Constants.START_DESCRIPTION;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class MyMegaBotPro extends AbilityBot {

    private final ResponseHandler responseHandler;
    private final AbilityService abilityService;

    @Autowired
    public MyMegaBotPro(@Value("${botToken}") String botToken,
                        @Value("${botUsername}") String botName) {
        super(botToken, botName);
        responseHandler = new ResponseHandler(silent, db);
        abilityService = new AbilityService(silent, db);
    }

    public Ability start(){
        return Ability
                .builder()
                .name("start")
                .info(START_DESCRIPTION)
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx ->  abilityService.toStart(ctx.chatId(), ctx.user()))
                .build();
    }

    public Ability stop(){
        return Ability
                .builder()
                .name("stop")
                .info("stop chat")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx ->  abilityService.stopChat(ctx.chatId()))
                .build();
    }

    public Ability change(){
        return Ability
                .builder()
                .name("change")
                .info("change parameters")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx ->  abilityService.toChange(ctx.chatId()))
                .build();
    }

    public Ability show(){
        return Ability
                .builder()
                .name("show")
                .info("show parameters")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx ->  abilityService.toShow(ctx.chatId()))
                .build();
    }

    public Reply replyToText() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) ->
                responseHandler.replyToText(
                        getChatId(upd),
                        upd.getMessage());
        return Reply.of(action, Flag.TEXT, upd -> responseHandler.userIsActive(getChatId(upd)));
    }

    public Reply replyToCallBackData(){
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) ->
        {
            try {
                responseHandler.replyToCallBackData(
                        getChatId(upd),
                        upd.getCallbackQuery().getData(),
                        upd.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return Reply.of(action, Flag.CALLBACK_QUERY, Update::hasCallbackQuery);
    }


    @Override
    public void onUpdateReceived(Update update) {
        super.onUpdateReceived(update);
        System.out.println("Received update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            System.out.println("Message text: " + messageText);
            if (messageText.equals("/start")) {
                // Відправити відповідь на команду /start
                System.out.println(update.getMessage().getChatId().toString() + " Bot is active!");
            }
        }
    }

    @Override
    public long creatorId() {
        return 429438291L;
    }
}
