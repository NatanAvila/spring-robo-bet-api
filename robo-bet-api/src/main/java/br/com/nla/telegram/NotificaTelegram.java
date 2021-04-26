package br.com.nla.telegram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.nla.entidade.Jogo;
import br.com.nla.util.EventObserver;

@Component
@EnableScheduling
public class NotificaTelegram {

	@Autowired
	private EventObserver eventObserver;

	private Set<Long> chats;

	private TelegramBot bot;

	private Map<Long, List<Jogo>> jogosNotificados;

	@PostConstruct
	private void init() {
		bot = new TelegramBot("1720231998:AAHVvOKUdJQ226igymtSZN5OL1678Ui5CYY");
		bot.setUpdatesListener(new UpdatesListener() {

			@Override
			public int process(List<Update> updates) {
				for (var update : updates) {
					chats.add(update.message().chat().id());
				}
				return 0;
			}
		});
		chats = new HashSet<>();
		jogosNotificados = new HashMap<>();
	}

	@Scheduled(cron = "* */1 * * * *")
	public void notificaTelegram() {
		eventObserver.getJogos().stream().filter(Jogo::isCompleto).filter(jg -> !jg.isNotificado()).forEach(jogo -> {
			StringBuilder msg = new StringBuilder();
			msg.append("Jogo: ");
			msg.append(jogo.getTitulo());
			msg.append("\n");
			msg.append("LINK: ");
			msg.append(jogo.getUrl());
			msg.append("\n");

			for (var mercado : jogo.getMercados()) {
				msg.append(mercado.getNome());
				msg.append(": ");
				for (var odd : mercado.getOdds()) {
					msg.append(odd.getNome());
					msg.append(" ");
					msg.append("ODD: ");
					msg.append(odd.getValor());
					msg.append("\n");
				}
				msg.append("\n");
			}

			if (!CollectionUtils.isEmpty(chats)) {
				for (var chat : chats) {
					boolean naoContemKey = !jogosNotificados.containsKey(chat);
					if (naoContemKey || !jogosNotificados.get(chat).contains(jogo)) {
						bot.execute(new SendMessage(chat, msg.toString()));
						if (naoContemKey) {
							var list = new ArrayList<Jogo>();
							list.add(jogo);
							jogosNotificados.put(chat, list);
						} else {
							jogosNotificados.get(chat).add(jogo);
						}
					}
				}
			}
		});
	}

}
