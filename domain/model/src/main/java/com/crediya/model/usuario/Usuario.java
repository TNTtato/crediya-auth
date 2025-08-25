package com.crediya.model.usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentidad;
    private String telefono;
    private Integer idRol;
    private Double salarioBase;
}
