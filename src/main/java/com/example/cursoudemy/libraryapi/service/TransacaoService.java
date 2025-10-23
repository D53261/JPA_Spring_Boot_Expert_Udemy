package com.example.cursoudemy.libraryapi.service;

// Importa as classes necessárias
import com.example.cursoudemy.libraryapi.models.Autor;
import com.example.cursoudemy.libraryapi.models.GeneroLivro;
import com.example.cursoudemy.libraryapi.models.Livro;
import com.example.cursoudemy.libraryapi.repository.AutorRepository;
import com.example.cursoudemy.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

// Indica que esta classe é um serviço do Spring
@Service
public class TransacaoService {
    // Injeta o repositório de autores
    @Autowired
    private AutorRepository autorRepository;

    // Injeta o repositório de livros
    @Autowired
    private LivroRepository livroRepository;

    /**
     * Método anotado com @Transactional: Garante que todas as operações dentro dele ocorram dentro de uma transação.
     * - @Transactional no Spring gerencia automaticamente o início, commit e rollback da transação.
     * - Se uma exceção não tratada ocorrer, a transação é revertida (rollback), desfazendo todas as mudanças no banco.
     * - Entidades carregadas ou salvas dentro da transação ficam em estado "managed" (anexadas ao Persistence Context),
     *   permitindo que alterações sejam detectadas e persistidas automaticamente no commit.
     * - Neste exemplo, simula salvar um livro e sua foto: se o salvamento da foto falhar, tudo é revertido.
     */
    @Transactional
    public void salvarLivroComFoto() {
        // salvar o livro
        // repository.save(livro);

        //pega o id do livro = livro.getId()
        // var id = livro.getId();

        //salva foto do livro -> bucket na nuvem (serviço externo)
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome do arquivo que foi salvo
        // livro.setNomeArquivoFoto(id + ".png");

        // Neste caso, já que a entidade está no estado Managed (dentro da transação), não é necessário salvar duas vezes no mesmo código,
        // pois a anotação @Transactional cuidará do flush e commit automaticamente para o banco de dados.
        // Se qualquer operação falhar (ex.: erro no serviço externo), a transação inteira é revertida.
    }

    /**
     * Método anotado com @Transactional: Demonstra como alterações em entidades "managed" são persistidas automaticamente.
     * - @Transactional cria um contexto transacional onde entidades recuperadas (ex.: via findById) ficam "managed".
     * - Mudanças em campos de uma entidade managed são detectadas pelo JPA/Hibernate e sincronizadas com o banco no commit (flush).
     * - Não é necessário chamar save() explicitamente para alterações em managed; o JPA cuida disso.
     * - Se a transação terminar sem erro, as mudanças são confirmadas (committed). Caso contrário, rollback.
     * - Este método ilustra o comportamento "atualização sem atualizar" (sem chamar save), confiando no estado managed.
     */
    @Transactional
    public void atualizacaoSemAtualizar() {
        // Observação sobre o comportamento aqui:
        // - Dentro de `@Transactional`, `findById(...)` retorna, quando existe, uma entidade no estado *managed*
        //   (anexada ao persistence context). Alterar campos dessa entidade (por exemplo `setDataPublicacao`)
        //   é detectado pelo provedor JPA e será persistido automaticamente no commit (flush).
        var livro = livroRepository
                .findById(UUID.fromString("ae194aee-6bf8-43d0-94fe-bec6ec322ef3"))
                .orElse(null); // Busca o livro pelo ID
        livro.setDataPublicacao(LocalDate.of(2023, 1, 1)); // Atualiza a data de publicação sem ser necessário chamar a função save
        // Como a entidade está managed, a alteração será persistida automaticamente no final da transação (commit).
    }

    /**
     * Método anotado com @Transactional: Exemplo completo de transação com criação, salvamento e possível rollback.
     * - @Transactional garante atomicidade: todas as operações (saveAndFlush) são executadas como uma unidade.
     * - saveAndFlush() força o flush imediato (sincroniza com o banco antes do commit final), útil para detectar erros cedo.
     * - Se uma condição for atendida (ex.: nome do autor), uma exceção é lançada, causando rollback automático.
     * - Sem @Transactional, cada save() seria uma transação separada, e erros não reverteriam operações anteriores.
     * - Este método ensina como transações previnem inconsistências: se "Rollback" for lançado, autor e livro não são salvos.
     */
    // Define que o método será executado dentro de uma transação
    @Transactional
    public void executarTransacao() {
        // Cria uma nova instância de Autor e define seus atributos
        Autor autor = new Autor();
        autor.setNome("Teste Fransisco");
        autor.setNacionalidade("Britânico");
        autor.setDataNascimento(LocalDate.of(1992, 1, 3));

        // Salva o autor no banco de dados imediatamente (flush força sincronização antes do commit final)
        autorRepository.saveAndFlush(autor);

        // Cria um novo livro e define seus atributos, associando ao autor criado
        Livro livro = new Livro();
        livro.setIsbn("578-81-68090-00-1");
        livro.setTitulo("Teste Livro do Fransisco");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIÊNCIA);
        livro.setDataPublicacao(LocalDate.of(1707, 10,05));
        livro.setAutor(autor);

        // Salva o livro no banco de dados imediatamente (flush detecta violações de constraints cedo)
        livroRepository.saveAndFlush(livro); // Se fosse apenas save, o livro não seria salvo antes do commit

        // Se o nome do autor for "Teste Fransisco", lança uma exceção para forçar o rollback da transação
        if (autor.getNome().equalsIgnoreCase("Teste Fransisco")) {
            throw new RuntimeException("Rollback"); // Causa rollback: autor e livro não são persistidos
        }
    }
}
