package br.com.nla.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import br.com.nla.entidade.Jogo;
import br.com.nla.entidade.Mercado;
import br.com.nla.repository.JogoRepository;
import lombok.Getter;

@Service
@EnableScheduling
@Singleton
public class EventObserver {

	@Autowired
	private JogoRepository jogoRepository;

	@Getter
	private Set<Jogo> jogos;

	@PostConstruct
	public void init() {
		jogos = jogoRepository.findAll().stream().collect(Collectors.toSet());
	}

	public Jogo completarJogo(Jogo jogo) {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder(new URI(jogo.getUrl())).GET().build();
			var response = client.send(request, BodyHandlers.ofString());
			String _substring = response.body().substring(response.body().indexOf("<script>window[\"initial_state\"]"));
			_substring = _substring.substring(_substring.indexOf("=") + 1, _substring.lastIndexOf("}") + 1);
			JSONObject jsonObject = new JSONObject(_substring);
			JSONObject data = jsonObject.getJSONObject("data");
			JSONObject event = data.getJSONObject("event");

			JSONArray mercadosPrincipais = event.getJSONArray("markets");

			for (var obj : mercadosPrincipais) {
				JSONObject jsMercado = new JSONObject(obj.toString());
				String nomeMercado = jsMercado.getString("name").toLowerCase();
				if (isMercadoPermitido(nomeMercado)) {
					jogo.getMercados().add(new Mercado(jsMercado, jogo));
				}
			}
			return jogoRepository.save(jogo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isMercadoPermitido(String nomeMercado) {
		return nomeMercado.equalsIgnoreCase("Resultado Final") || nomeMercado.contains("asiatico")
				|| nomeMercado.contains("asiático");
	}

}
