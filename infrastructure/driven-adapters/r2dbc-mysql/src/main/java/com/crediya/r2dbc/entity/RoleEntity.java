package com.crediya.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="rol")
@Data @AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @Column("UniqueID")
    Integer id;
    @Column("nombre")
    String name;
    @Column("descripcion")
    String description;
}
