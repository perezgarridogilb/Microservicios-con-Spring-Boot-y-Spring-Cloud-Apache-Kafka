package model;


import org.springframework.data.annotation.Transient;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(value = "products")
public class Producto implements Persistable<Integer> {
    @Id
    @Column(value = "codProducto")
    private Integer codProducto;
    private String nombre;
    private String categoria;
    @Column(value = "precioUnitario")
    private double precioUnitario;
    private int stock;
    @Transient
    private Boolean nuevo;

    @Override
    public @Nullable Integer getId() {
        return codProducto;
    }
    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return nuevo;
    }
}
