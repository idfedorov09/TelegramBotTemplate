package ru.idfedorov09.telegram.bot.base.controller

import kotlinx.coroutines.Dispatchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.idfedorov09.telegram.bot.base.UpdatesHandler
import ru.idfedorov09.telegram.bot.base.UpdatesSender
import ru.idfedorov09.telegram.bot.base.data.GlobalConstants.QUALIFIER_FLOW_TG_BOT
import ru.idfedorov09.telegram.bot.base.flow.ExpContainer
import ru.mephi.sno.libs.flow.belly.FlowBuilder
import ru.mephi.sno.libs.flow.belly.FlowContext

@Component
class UpdatesController : UpdatesSender(), UpdatesHandler {

    @Autowired
    @Qualifier(QUALIFIER_FLOW_TG_BOT)
    private lateinit var flowBuilder: FlowBuilder

    // @Async("infinityThread") // if u need full async execution
    override fun handle(telegramBot: TelegramLongPollingBot, update: Update) {
        // Во время каждой прогонки графа создается свой контекст,
        // в который кладется бот и само обновление
        val flowContext = FlowContext()

        // прогоняем граф с ожиданием
        flowBuilder.initAndRun(
            flowContext = flowContext,
            dispatcher = Dispatchers.Default,
            wait = true,
            ExpContainer(), // экспы
            telegramBot,
            update,
        )
    }
}