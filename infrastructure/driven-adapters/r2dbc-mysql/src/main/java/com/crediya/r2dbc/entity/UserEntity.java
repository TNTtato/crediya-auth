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
public class UserEntity {
    @Id
    @Column("id_usuario")
    Integer userId;
    @Column("nombre")
    String name;
    @Column("apellido")
    String lastName;
    String email;
    @Column("telefono")
    String phone;
    @Column("documento_identidad")
    String cardId;
    @Column("salario_base")
    Double baseSalary;
    @Column("id_rol")
    Integer roleId;
}
