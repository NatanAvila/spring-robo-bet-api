package br.com.nla.schedule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.nla.entidade.Jogo;
import br.com.nla.util.EventObserver;

@Component
@EnableScheduling
public class JogosFutebolSchedule {

	@Autowired
	private EventObserver observer;

	@Scheduled(cron = "* */1 * * * *")
	public void verificaJogosFutebol() {
		try {
			List<String> urls = new ArrayList<>();
			urls.add("https://br.betano.com/sport/futebol/competicoes/brasil/10004/");
//			urls.add("https://br.betano.com/sport/futebol/america-clubes/copa-libertadores/16775/");
//			urls.add("https://br.betano.com/sport/futebol/liga-dos-campeoes/182748/");
//			urls.add("https://br.betano.com/sport/futebol/espanha/5,10000,17592,193746/");
			HttpClient client = HttpClient.newHttpClient();
			for (var url : urls) {
				HttpRequest request = HttpRequest.newBuilder(new URI(url)).GET().build();
				observaNovosJogos(client, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void observaNovosJogos(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
		JSONArray array = getArrayJogos(client, request);
		for (var obj : array) {
			var js = new JSONObject(obj.toString());
			OffsetDateTime odt = OffsetDateTime.parse(js.getString("startDate"));
			LocalDateTime datetime = odt.toLocalDateTime();
			if (LocalDateTime.now().plusMinutes(5).isBefore(datetime)) {
				Jogo jogo = new Jogo(js);
				if (observer.getJogos().stream()
						.noneMatch(objJogo -> Objects.equals(jogo.getUrl(), objJogo.getUrl()))) {
					observer.getJogos().add(jogo);
				}
			}
		}
	}

	private JSONArray getArrayJogos(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
		var response = client.send(request, BodyHandlers.ofString());
		String inicioScript = response.body()
				.substring(response.body().indexOf("<script type=\"application/ld+json\">"));
		var fimScript = inicioScript.substring(inicioScript.indexOf("["), inicioScript.indexOf("</script>"));

		JSONArray array = new JSONArray(fimScript);
		return array;
	}
}
