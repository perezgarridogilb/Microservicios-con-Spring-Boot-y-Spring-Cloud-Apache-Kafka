package service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import model.Elemento;
import reactor.core.publisher.Flux;

@Service
public class ElementosServiceImpl implements ElementosService {
    String url="http://127.0.0.1:8080";
    @Autowired
    WebClient webClient;
    @Override
    public Flux<Elemento> elementosPorPrecio(double precioMax) {
        //WebClient webClient = WebClient.create(url);
        return webClient
        .get()
        .uri(url+"elementos/"+precioMax)
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(Elemento.class);
    }

}
