package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import model.Elemento;
import reactor.core.publisher.Flux;
import service.ElementosService;

@RestController
public class ElementosController {

    @Autowired
    private ElementosService elementosService;

    // Endpoint único para filtrar por precio máximo
    @GetMapping("/elementos/{precio}")
    public Flux<Elemento> elementoPrecio(@PathVariable("precio") double precioMax){
        return elementosService.elementosPrecioMax(precioMax);
    }

}