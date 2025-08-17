package com.example.festas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class TemaFesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do tema é obrigatório")
    private String nome;

    private String descricao;

    private BigDecimal precoBase;

    private Boolean ativo;

    @ManyToMany(mappedBy = "temas")
    @JsonIgnoreProperties("temas")
    private List<SolicitacaoOrcamento> solicitacoes;
}