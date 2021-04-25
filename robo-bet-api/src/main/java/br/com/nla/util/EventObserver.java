package br.com.nla.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.nla.entidade.Jogo;
import lombok.Getter;

@Service
@EnableScheduling
public class EventObserver {

	@Getter
	public Set<Jogo> jogos;

	@PostConstruct
	public void init() {
		jogos = new HashSet<>();
	}

	@Scheduled(cron = "* */1 * * * *")
	public void confereJogos() {
		jogos.parallelStream().filter(jg -> !jg.isCompleto()).forEach(this::completarJogo);
	}

	private void completarJogo(Jogo jogo) {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder(new URI(jogo.getUrl())).GET().build();
			var response = client.send(request, BodyHandlers.ofString());
			String _substring = response.body().substring(response.body().indexOf("<script>window[\"initial_state\"]"));
			_substring = _substring.substring(_substring.indexOf("=") + 1, _substring.lastIndexOf("}") + 1);
			JSONObject jsonObject = new JSONObject(_substring);
			JSONObject data = jsonObject.getJSONObject("data");
			System.out.println(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
