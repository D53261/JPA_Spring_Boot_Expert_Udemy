package com.example.cursoudemy.libraryapi.repository;

import com.example.cursoudemy.libraryapi.models.Autor;
import com.example.cursoudemy.libraryapi.models.GeneroLivro;
import com.example.cursoudemy.libraryapi.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Interface de repositório para a entidade Livro, estendendo JpaRepository para fornecer operações CRUD básicas
 * e consultas customizadas. Utiliza Spring Data JPA para gerar automaticamente queries baseadas nos nomes dos métodos
 * e permite o uso de @Query para JPQL ou SQL nativo.
 */
public interface LivroRepository extends JpaRepository<Livro, UUID> {

    // Query method: Busca todos os livros associados a um autor específico.
    // select * from livro where autor_id = ?
    List<Livro> findByAutor(Autor autor);

    // Query method: Busca livros pelo título exato.
    // select * from livro where titulo = ?
    List<Livro> findByTitulo(String titulo);

    // Query method: Busca livros pelo ISBN exato.
    // select * from livro where isbn = ?
    List<Livro> findByIsbn(String isbn);

    // Query method: Busca livros que correspondam ao título e preço especificados.
    // select * from livro where titulo = ? and preco = ?
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    // Query method: Busca livros que correspondam ao título OU ao ISBN.
    // select * from livro where titulo = ? or isbn = ?
    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    // Query method: Busca livros publicados entre duas datas (inclusivo).
    // select * from livro where data_publicacao between ? and ?
    List<Livro> findByDataPublicacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    // JPQL -> referencia as entidades e as propriedades
    // Query customizada: Lista todos os livros ordenados por título e preço.
    // select l.* from livro as l order by l.titulo, l.preco
    @Query(" select l from Livro l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdenadoPorTituloAndPreco();

    /**
     * Query customizada com JOIN: Lista todos os autores associados aos livros (evita duplicatas se um autor tiver múltiplos livros).
     * select a.*
     * from livro l
     * join autor a on a.id = l.id_autor
     */
    @Query(" select a from Livro l join l.autor a ")
    List<Autor> listarAutoresDosLivros();

    /**
     * Query customizada: Lista títulos distintos de livros (útil para evitar duplicatas em listas de nomes).
     * select distinct l.titulo
     * from livro l
     */
    @Query(" select distinct l.titulo from Livro l ")
    List<String> listarNomesDiferentesLivros();
    // OBS.: caso eu tentasse puxar uma lista de livros com essa query daria erro, pois o JPA não permite
    // selecionar apenas parte de uma entidade (como o título) sem usar um DTO ou algo do tipo para ser impresso

    // Query customizada com JOIN e filtro: Lista livros de autores britânicos, ordenados por preço.
    @Query("""
            select l
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Britânico'
            order by l.preco
            """)
    List<Livro> listarLivrosAtoresBritanicos();

    // JPQL com parâmetro -> parametro nomeado
    // Query customizada: Busca livros de um gênero específico, ordenados por preço.
    // select * from livro where genero = ?
    @Query(" select l from Livro l where l.genero = :genero order by l.preco ")
    List<Livro> findByGenero(
            @Param("genero") GeneroLivro generoLivro
    );

    // positional parameters
    // Query customizada: Busca livros de um gênero específico usando parâmetros posicionais, ordenados por preço.
    // select * from livro where genero = ?
    @Query(" select l from Livro l where l.genero = ?1 order by l.preco ")
    List<Livro> findByGeneroPositionalParameters(
            @Param("genero") GeneroLivro generoLivro
    );

    // JPQL para escrita (update, delete)
    // Query de modificação: Deleta todos os livros de um gênero específico.
    @Modifying // indica que a query é de modificação (insert, update, delete) e pode modificar registros
    @Transactional // indica que a query é transacional, ou seja, deve ser executada dentro de uma transação do banco
    @Query(" delete from Livro where genero = ?1 ")
    void deleteByGenero(GeneroLivro generoLivro);

    // Query de modificação: Atualiza a data de publicação de todos os livros para uma data específica.
    @Modifying
    @Transactional
    @Query(" update Livro set dataPublicacao = ?1 ")
    void atualizarDataDePublicacaoDeLivros(LocalDate data);
}
