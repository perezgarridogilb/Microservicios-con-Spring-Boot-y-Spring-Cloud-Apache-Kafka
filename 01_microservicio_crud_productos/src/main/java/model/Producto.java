package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // constructor sin param
@AllArgsConstructor // constructor con param
@Data // getter y setter
public class Producto {
private int codProducto;
private String nombre;
private String categoria;
private double precioUnitario;
private int stock;
}
