package com.example.festas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Entity
@Getter
@Setter
public class TipoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do tipo de evento é obrigatório")
    private String nome;

    private String descricao;

    private Integer capacidadeMinima;

    private Integer capacidadeMaxima;

    @OneToMany(mappedBy = "tipoEvento")
    @JsonIgnoreProperties("tipoEvento")
    private List<SolicitacaoOrcamento> solicitacoes;

    public void setId(Long id) {
    }
}