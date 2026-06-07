package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(value = "products")
public class Producto {
    @Id
    private int codProducto;
    private String nombre;
    private String categoria;
    private double precioUnitario;
    private int stock;
}
