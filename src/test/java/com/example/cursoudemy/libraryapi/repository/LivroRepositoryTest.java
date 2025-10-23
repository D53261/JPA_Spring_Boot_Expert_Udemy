package com.example.cursoudemy.libraryapi.repository; // Define o pacote do repositório

import com.example.cursoudemy.libraryapi.models.Autor; // Importa a classe Autor para associações em testes
import com.example.cursoudemy.libraryapi.models.GeneroLivro; // Importa o enum GeneroLivro para filtros e validações
import com.example.cursoudemy.libraryapi.models.Livro; // Importa a classe Livro, entidade principal dos testes
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação para injeção de dependências do Spring
import org.springframework.boot.test.context.SpringBootTest; // Importa a anotação para configurar testes com contexto Spring Boot
import org.springframework.transaction.annotation.Transactional; // Importa a anotação para gerenciar transações em testes

import java.math.BigDecimal; // Importa BigDecimal para manipulação de valores monetários em testes
import java.time.LocalDate; // Importa LocalDate para trabalhar com datas de publicação
import java.util.List; // Importa List para armazenar resultados de consultas
import java.util.UUID; // Importa UUID para identificadores únicos de entidades

import org.junit.jupiter.api.Test; // Importa a anotação @Test do JUnit 5 para definir métodos de teste

/**
 * Classe de teste para o LivroRepository, demonstrando operações CRUD e consultas customizadas com Spring Data JPA.
 * Cada teste valida funcionalidades como salvar, atualizar, deletar e buscar livros, ilustrando conceitos como
 * estados de entidade, transações e uso de JPQL. Utiliza @SpringBootTest para carregar o contexto completo da aplicação,
 * permitindo injeção de dependências reais (não mocks).
 */
@SpringBootTest // Indica que o teste roda com o contexto completo do Spring Boot, incluindo banco de dados
public class LivroRepositoryTest {

    @Autowired // Injeta o repositório de Livro para executar operações de teste
    LivroRepository repository;

    @Autowired // Injeta o repositório de Autor para buscar autores associados aos livros
    AutorRepository autores;

    /**
     * Teste para demonstrar a operação de salvar (persistir) um novo livro no banco de dados.
     * Cria uma instância de Livro, define seus atributos e associa a um autor existente.
     * Valida o mapeamento @Entity e o uso de save() para inserir dados.
     */
    @Test
    public void salvarTest() {
        Livro livro = new Livro(); // Cria uma nova instância de Livro (estado Transient)
        livro.setIsbn("978-85-85940-00-1"); // Define o ISBN único do livro
        livro.setTitulo("Java 100"); // Define o título do livro
        livro.setPreco(BigDecimal.valueOf(139)); // Define o preço usando BigDecimal para precisão
        livro.setGenero(GeneroLivro.FICCAO); // Define o gênero usando o enum GeneroLivro
        livro.setDataPublicacao(LocalDate.of(2054, 10, 05)); // Define a data de publicação

        // Busca um autor existente pelo ID para associar ao livro (demonstra relacionamento @ManyToOne)
        Autor autor = autores.findById(UUID.fromString("9897bbfa-8d2a-41bd-bfb1-7d588c82a83d"))
                .orElse(null);

        livro.setAutor(autor); // Associa o autor ao livro

        repository.save(livro); // Salva o livro no banco (transita para estado Managed e persiste)
    }

    /**
     * Teste para demonstrar a atualização de um livro existente, alterando seu autor e preço.
     * Busca um livro pelo ID, modifica seus campos e salva as alterações.
     * Ilustra como entidades managed permitem alterações automáticas sem necessidade de merge().
     */
    @Test
    void atualizarAutorDoLivroTest() {
        UUID id = UUID.fromString("f78fda87-d813-4069-8488-8f3a31721986"); // ID do livro a ser atualizado
        var livroParaAtualizar = repository.findById(id).orElse(null); // Busca o livro (torna-o managed)

        // Busca um novo autor para associar
        UUID autorId = UUID.fromString("be80cafb-0c57-4ca9-a279-faf0695490f1");
        Autor autor = autores.findById(autorId).orElse(null);

        livroParaAtualizar.setAutor(autor); // Atualiza o autor (mudança detectada automaticamente)
        livroParaAtualizar.setPreco(BigDecimal.valueOf(150.0)); // Atualiza o preço

        repository.save(livroParaAtualizar); // Salva as alterações (opcional em managed, mas explícito aqui)
    }

    /**
     * Teste para demonstrar a operação de deletar um livro pelo ID.
     * Remove a entidade do banco, ilustrando o uso de deleteById() e o estado Removed.
     */
    @Test
    void deletarTest() {
        UUID id = UUID.fromString("ef7744b4-a609-435b-b1fd-20d738726c6e"); // ID do livro a ser deletado
        repository.deleteById(id); // Remove o livro do banco de dados
    }

    /**
     * Teste para demonstrar a busca de um livro pelo ID, incluindo carregamento lazy de relacionamentos.
     * Usa @Transactional para garantir que o acesso ao autor (relacionamento lazy) seja possível.
     * Exibe informações do livro e autor, mostrando como o JPA carrega dados sob demanda.
     */
    @Test
    @Transactional // Necessário para acessar relacionamentos lazy dentro do teste
    void buscarLivroTest() {
        UUID id = UUID.fromString("6c1cc25e-1ecc-4c89-b093-0512d3f68eaa"); // ID do livro a ser buscado
        Livro livro = repository.findById(id).orElse(null); // Busca o livro (managed)
        System.out.println("Livro: "); // Exibe cabeçalho para o título
        System.out.println(livro.getTitulo()); // Exibe o título do livro
        System.out.println("Autor: "); // Exibe cabeçalho para o autor
        System.out.println(livro.getAutor().getNome()); // Exibe o nome do autor (carregamento lazy)
    }

    /**
     * Teste para demonstrar consultas por título usando Query Methods (findByTitulo).
     * Busca livros com título exato e exibe os resultados, ilustrando geração automática de SQL.
     */
    @Test
    void pesquisaPorTituloTest() {
        List<Livro> lista = repository.findByTitulo("Livro do games: Ato 2"); // Busca por título exato
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas por ISBN usando Query Methods (findByIsbn).
     * Busca livros com ISBN exato, validando unicidade e mapeamento de campos.
     */
    @Test
    void pesquisaPorISBNTest() {
        List<Livro> lista = repository.findByIsbn("285-81-94830-00-1"); // Busca por ISBN exato
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas com múltiplos critérios (título e preço) usando Query Methods (findByTituloAndPreco).
     * Combina condições AND, mostrando como o Spring Data gera SQL complexo automaticamente.
     */
    @Test
    void pesquisaPorTituloEPrecoTest() {
        List<Livro> lista = repository.findByTituloAndPreco("Livro do games: Ato 2", BigDecimal.valueOf(150)); // Busca por título E preço
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas customizadas com JPQL (listarTodosOrdenadoPorTituloAndPreco).
     * Lista todos os livros ordenados por título e preço, ilustrando o uso de @Query com JPQL.
     */
    @Test
    void listarLivrosComQueryJPQLTest() {
        List<Livro> lista = repository.listarTodosOrdenadoPorTituloAndPreco(); // Executa query JPQL customizada
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas com JOIN em JPQL (listarAutoresDosLivros).
     * Lista autores associados a livros, mostrando como JPQL lida com relacionamentos.
     */
    @Test
    void listarAutoresDosLivrosTest() {
        List<Autor> lista = repository.listarAutoresDosLivros(); // Busca autores via JOIN
        lista.forEach(System.out::println); // Exibe cada autor encontrado
    }

    /**
     * Teste para demonstrar consultas com DISTINCT em JPQL (listarNomesDiferentesLivros).
     * Lista títulos únicos de livros, evitando duplicatas e mostrando projeções parciais.
     */
    @Test
    void listarTitulosNaoRepetidosTest() {
        List<String> lista = repository.listarNomesDiferentesLivros(); // Busca títulos distintos
        lista.forEach(System.out::println); // Exibe cada título encontrado
    }

    /**
     * Teste para demonstrar consultas com filtros complexos em JPQL (listarLivrosAtoresBritanicos).
     * Lista livros de autores britânicos, ordenados por preço, ilustrando JOIN e WHERE.
     */
    @Test
    void listarLivrosDeAutoresBritanicos() {
        List<Livro> lista = repository.listarLivrosAtoresBritanicos(); // Busca livros com filtro de nacionalidade
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas com parâmetros nomeados em JPQL (findByGenero).
     * Busca livros por gênero usando @Param, mostrando flexibilidade em filtros dinâmicos.
     */
    @Test
    void listarPorGeneroQueryParam() {
        List<Livro> lista = repository.findByGenero(GeneroLivro.FICCAO); // Busca por gênero com parâmetro nomeado
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar consultas com parâmetros posicionais em JPQL (findByGeneroPositionalParameters).
     * Busca livros por gênero usando ?1, alternativa aos parâmetros nomeados.
     */
    @Test
    void listarPorGeneroPositionalParam() {
        List<Livro> lista = repository.findByGeneroPositionalParameters(GeneroLivro.FICCAO); // Busca por gênero com parâmetro posicional
        lista.forEach(System.out::println); // Exibe cada livro encontrado
    }

    /**
     * Teste para demonstrar operações de modificação (delete) com @Modifying e @Transactional (deleteByGenero).
     * Deleta livros de um gênero específico, ilustrando updates em lote e necessidade de transação.
     */
    @Test
    void deletePorGeneroTest() {
        repository.deleteByGenero(GeneroLivro.CIÊNCIA); // Deleta livros do gênero CIÊNCIA
    }

    /**
     * Teste para demonstrar operações de modificação (update) com @Modifying e @Transactional (atualizarDataDePublicacaoDeLivros).
     * Atualiza a data de publicação de todos os livros, mostrando updates em massa.
     */
    @Test
    void updateDataPublicacaoTest() {
        repository.atualizarDataDePublicacaoDeLivros(LocalDate.of(2020, 03, 12)); // Atualiza data de todos os livros
    }
}
