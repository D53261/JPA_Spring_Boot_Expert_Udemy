package com.example.cursoudemy.libraryapi.repository; // Define o pacote do repositório de testes

import com.example.cursoudemy.libraryapi.service.TransacaoService; // Importa o serviço responsável pelas operações de transação
import org.junit.jupiter.api.Test; // Importa a anotação @Test do JUnit 5 para definir métodos de teste
import org.springframework.beans.factory.annotation.Autowired; // Permite injeção automática de dependências do Spring
import org.springframework.boot.test.context.SpringBootTest; // Inicializa o contexto completo do Spring Boot para testes integrados
import org.springframework.transaction.annotation.Transactional; // Importa anotação para gerenciar transações em testes

/**
 * Classe de teste para o TransacaoService, demonstrando operações de transação no contexto do Spring.
 * Cada teste valida funcionalidades como execução de transações simples, rollback em caso de erro e estados de entidades gerenciadas.
 * Utiliza @SpringBootTest para contexto real, permitindo testes integrados com banco de dados e simulação de cenários transacionais.
 */
@SpringBootTest // Indica que o teste roda com o contexto completo do Spring Boot, incluindo injeção de dependências e banco
public class TransacoesTest { // Classe de teste para operações de transação

    @Autowired // Injeta o serviço de transação para executar operações transacionais
    TransacaoService transacaoService; // Serviço responsável pelas operações de transação

    /**
     * Begin -> Inicia uma transação
     * Commit -> Confirma a transação
     * Rollback -> Desfaz a transação
     */

    /**
     * Teste para demonstrar uma transação simples com múltiplas operações.
     * Executa uma sequência de ações: salvar autor, salvar livro associado, e simulações de aluguel, envio de email e notificação.
     * Se qualquer operação falhar, a transação é revertida (rollback), garantindo consistência no banco.
     */
    @Test
    void transacaoSimples() { // Testa uma transação completa com possíveis rollbacks
        // Chama o método que executa uma transação completa:
        // 1. Salva um autor no banco de dados
        // 2. Salva um livro associado a esse autor
        // 3. (Simulação) Poderia alugar um livro
        // 4. (Simulação) Poderia enviar email de confirmação
        // 5. (Simulação) Poderia notificar que o livro foi alugado
        // Se ocorrer uma exceção, todas as operações são desfeitas (rollback)
        transacaoService.executarTransacao(); // Executa a transação e valida o comportamento
    }

    /**
     * Teste para demonstrar o estado Managed de entidades em uma transação.
     * Executa uma atualização sem salvar explicitamente, confiando no gerenciamento automático do JPA.
     * Mostra como mudanças em entidades gerenciadas são detectadas e persistidas no commit da transação.
     */
    @Test
    void transacaoEstadoManaged() { // Testa atualização em estado Managed sem save explícito
        transacaoService.atualizacaoSemAtualizar(); // Executa a atualização e valida o estado Managed
    }
}
