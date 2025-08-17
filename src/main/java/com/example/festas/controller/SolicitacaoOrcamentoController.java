package com.example.festas.controller;

import com.example.festas.entity.SolicitacaoOrcamento;
import com.example.festas.service.SolicitacaoOrcamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoOrcamentoController {

    @Autowired
    private SolicitacaoOrcamentoService solicitacaoService;

    @GetMapping
    public ResponseEntity<List<SolicitacaoOrcamento>> buscarTodos() {
        List<SolicitacaoOrcamento> solicitacoes = solicitacaoService.buscarTodos();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoOrcamento> buscarPorId(@PathVariable Long id) {
        Optional<SolicitacaoOrcamento> solicitacao = solicitacaoService.buscarPorId(id);
        return solicitacao.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitacaoOrcamento> salvar(@Valid @RequestBody SolicitacaoOrcamento solicitacao) {
        try {
            SolicitacaoOrcamento solicitacaoSalva = solicitacaoService.salvar(solicitacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoSalva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoOrcamento> atualizar(@PathVariable Long id, @Valid @RequestBody SolicitacaoOrcamento solicitacao) {
        try {
            SolicitacaoOrcamento solicitacaoAtualizada = solicitacaoService.atualizar(id, solicitacao);
            return ResponseEntity.ok(solicitacaoAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            solicitacaoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<SolicitacaoOrcamento>> buscarPorStatus(@RequestParam String status) {
        List<SolicitacaoOrcamento> solicitacoes = solicitacaoService.buscarPorStatus(status);
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SolicitacaoOrcamento>> buscarPorCliente(@PathVariable Long clienteId) {
        List<SolicitacaoOrcamento> solicitacoes = solicitacaoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(solicitacoes);
    }
}