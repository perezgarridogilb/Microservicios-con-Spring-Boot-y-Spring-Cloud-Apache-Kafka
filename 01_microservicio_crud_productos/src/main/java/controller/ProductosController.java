package controller;

import java.util.List;

import javax.print.attribute.standard.Media;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Producto;
import service.ProductosService;

@RestController
public class ProductosController {

    @Autowired
    ProductosService productosService;

    @GetMapping(value = "productos", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public List<Producto> productos() {
        return productosService.catalogo();
    }
    @GetMapping(value = "productos/{categoria}")
    public List<Producto> productosCategoria(@PathVariable("categoria") String categoria) {
        return productosService.productosCategoria(categoria);
    }
        @GetMapping(value = "producto")
    public Producto productosCodigo(@RequestParam( ) int cod) {
        return productosService.productoCodigo(cod);
    }

            @PostMapping(value = "alta", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public void altaProducto(@RequestBody Producto producto) {
        productosService.altaProducto(producto);
    }
    @DeleteMapping(value = "eliminar")
        public Producto eliminarProducto(@RequestParam("cod") int cod) {
            return productosService.eliminarProducto(cod);
    }
@PutMapping(value = "actualizar")
            public Producto actualizarProducto(@RequestParam("cod") int cod, @RequestParam("precio") double precio) {
            return productosService.actualizarPrecio(cod, precio);
    }
}
