package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import model.Elemento;
import reactor.core.publisher.Flux;
import service.ElementosService;

@Controller
public class ElementosController {

    @Autowired
    private ElementosService elementosService;

    // Endpoint único para filtrar por precio máximo
    // @GetMapping("/elementos/{precio}")
    @GetMapping(value = "buscar")
    public String buscar(@RequestParam("Precio") double precioMax, Model model){
        IReactiveDataDriverContextVariable reactive = new ReactiveDataDriverContextVariable(elementosService.elementosPorPrecio(precioMax), 1);
        model.addAttribute("resultado", reactive);
        return "listado";
    }

}