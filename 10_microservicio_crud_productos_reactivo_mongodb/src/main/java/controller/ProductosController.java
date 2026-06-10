package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.ProductosService;

@CrossOrigin(origins = "*")
@RestController
public class ProductosController {
    @Autowired
    ProductosService productosService;

    @GetMapping(value = "productos")
    public Flux<Producto> productos(@RequestParam(required = false) Integer cod) {
        if (cod != null) {
            return productosService.productoCodigo(cod).flux();
        }
        return productosService.catalogo();
    }
    

	@GetMapping(value = "productos/{categoria}")
	public ResponseEntity<Flux<Producto>> productosCategoria(@PathVariable("categoria") String categoria) {
		return new ResponseEntity<>(productosService.productosCategoria(categoria), HttpStatus.OK);
	}

        @GetMapping(value = "productos/{cod:\\d+}")
    public Mono<Producto> productosCodigo(@PathVariable int cod) {
        return productosService.productoCodigo(cod);
    }

            @PostMapping(value = "alta", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> altaProducto(@RequestBody Producto producto) {
        return productosService.altaProducto(producto);
    }
    @DeleteMapping(value = "eliminar/{cod}")
        public Mono<Producto> eliminarProducto(@PathVariable int cod) {
            return productosService.eliminarProducto(cod);
    }
@PutMapping(value = "actualizar/{cod}")
            public Mono<Producto> actualizarProducto(@PathVariable int cod, @RequestParam("precio") double precio) {
            return productosService.actualizarPrecio(cod, precio);

    }

}
