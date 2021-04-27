package br.com.nla.telegram;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import br.com.nla.entidade.Notificacao;
import br.com.nla.repository.NotificacaoRepository;
import br.com.nla.util.EventObserver;

@Component
@EnableScheduling
public class NotificaTelegram {

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private EventObserver eventObserver;

	private Set<Long> chats;

	private TelegramBot bot;

	private Set<Notificacao> notificacoes;

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
		notificacoes = notificacaoRepository.findAll().stream().collect(Collectors.toSet());
		chats = notificacoes.stream().map(Notificacao::getChat).collect(Collectors.toSet());
	}

	@Scheduled(cron = "* */1 * * * *")
	public synchronized void notificaTelegram() {
		boolean alteracao = false;
		for (var jogo : eventObserver.getJogos()) {

			if (!CollectionUtils.isEmpty(chats)) {
				for (var chat : chats) {
					if (notificacoes.stream().noneMatch(nt -> nt.getChat().equals(chat))) {
						notificacoes.add(new Notificacao(chat));
					}
					for (var nt : notificacoes.stream().filter(nt -> !nt.getJogos().contains(jogo))
							.collect(Collectors.toList())) {
						notificarChat(jogo, chat);
						nt.getJogos().add(jogo);
						alteracao = true;
					}
				}
			}
		}

		if (alteracao) {
			notificacaoRepository.saveAll(notificacoes);
		}
	}

	private String getMessage(Jogo jogo) {
		StringBuilder msg = new StringBuilder();
		msg.append("Jogo: ");
		msg.append(jogo.getTitulo());
		msg.append("\n");
		msg.append("LINK: ");
		msg.append(jogo.getUrl());
		msg.append("\n");

		for (var mercado : jogo.getMercados()) {
			msg.append(mercado.getNome());
			msg.append(": \n");
			for (var odd : mercado.getOdds()) {
				msg.append(odd.getNome());
				msg.append(" ");
				msg.append("ODD: ");
				msg.append(odd.getValor());
				msg.append("\n");
			}
			msg.append("\n");
		}
		return msg.toString();
	}

	private void notificarChat(Jogo jogo, Long chat) {
		bot.execute(new SendMessage(chat, getMessage(jogo)));
	}

}
