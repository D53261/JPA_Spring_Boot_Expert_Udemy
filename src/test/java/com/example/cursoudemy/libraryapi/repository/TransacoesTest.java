package com.example.cursoudemy.libraryapi.repository;

import com.example.cursoudemy.libraryapi.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// Indica que o teste deve rodar com o contexto completo do Spring Boot
@SpringBootTest
public class TransacoesTest {
    // Injeta o serviço responsável pelas operações de transação
    @Autowired
    TransacaoService transacaoService;
    /**
     * Begin -> Inicia uma transação
     * Commit -> Confirma a transação
     * Rollback -> Desfaz a transação
     */

    @Test
    void transacaoSimples() {
        // Chama o método que executa uma transação completa:
        // 1. Salva um autor no banco de dados
        // 2. Salva um livro associado a esse autor
        // 3. (Simulação) Poderia alugar um livro
        // 4. (Simulação) Poderia enviar email de confirmação
        // 5. (Simulação) Poderia notificar que o livro foi alugado
        // Se ocorrer uma exceção, todas as operações são desfeitas (rollback)
        transacaoService.executarTransacao();
    }

    @Test
    void transacaoEstadoManaged() {
        transacaoService.atualizacaoSemAtualizar();
    }
}