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

        // Neste caso, ja que esta transação esta no estado Managed, não será necessário salvar duas vezes no mesmo codigo, pois a anotation @Transactional cuidará disso no commit da transação para o banco de dados.
    }

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
    }

    // Define que o método será executado dentro de uma transação
    @Transactional
    public void executarTransacao() {
        // Cria uma nova instância de Autor e define seus atributos
        Autor autor = new Autor();
        autor.setNome("Teste Fransisco");
        autor.setNacionalidade("Britânico");
        autor.setDataNascimento(LocalDate.of(1992, 1, 3));

        // Salva o autor no banco de dados imediatamente
        autorRepository.saveAndFlush(autor);

        // Cria um novo livro e define seus atributos, associando ao autor criado
        Livro livro = new Livro();
        livro.setIsbn("578-81-68090-00-1");
        livro.setTitulo("Teste Livro do Fransisco");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIÊNCIA);
        livro.setDataPublicacao(LocalDate.of(1707, 10,05));
        livro.setAutor(autor);

        // Salva o livro no banco de dados imediatamente
        livroRepository.saveAndFlush(livro); // Se fosse apenas save, o livro não seria salvo antes do commit

        // Se o nome do autor for "Teste Fransisco", lança uma exceção para forçar o rollback da transação
        if (autor.getNome().equalsIgnoreCase("Teste Fransisco")) {
            throw new RuntimeException("Rollback");
        }
    }
}