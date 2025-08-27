package com.crediya.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity {
    @Id
    @Column("id_usuario")
    Integer idUsuario;
    String nombre;
    String apellido;
    String email;
    String telefono;
    @Column("documento_identidad")
    String documentoIdentidad;
    @Column("salario_base")
    Double salarioBase;
    @Column("id_rol")
    RolEntity idRol;
}
