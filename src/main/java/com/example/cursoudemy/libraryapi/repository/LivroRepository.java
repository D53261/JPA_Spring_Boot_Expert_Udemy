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

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    // select * from livro where autor_id = ?
    List<Livro> findByAutor(Autor autor);
    // select * from livro where titulo = ?
    List<Livro> findByTitulo(String titulo);
    // select * from livro where isbn = ?
    List<Livro> findByIsbn(String isbn);

    // select * from livro where titulo = ? and preco = ?
    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);
    // select * from livro where titulo = ? or isbn = ?
    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    // select * from livro where data_publicacao between ? and ?
    List<Livro> findByDataPublicacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    // JPQÇ -> referencia as entidades e as propriedades
    // select l.* from livro as l order by l.titulo
    @Query(" select l from Livro l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdenadoPorTituloAndPreco();

    /**
     * select a.*
     * from livro l
     * join autor a on a.id = l.id_autor
     */
    @Query(" select a from Livro l join l.autor a ")
    List<Autor> listarAutoresDosLivros();

    /**
     * select distinct l.titulo
     * from livro l
     */
    @Query(" select distinct l.titulo from Livro l ")
    List<String> listarNomesDiferentesLivros();
    // OBS.: caso eu tentasse puxar uma lista de livros com essa query daria erro, pois o JPA não permite
    // selecionar apenas parte de uma entidade (como o título) sem usar um DTO ou algo do tipo para ser impresso

    @Query("""
            select l
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Britânico'
            order by l.preco
            """)
    List<Livro> listarLivrosAtoresBritanicos();

    // JPQL com parâmetro -> parametro nomeado
    // select * from livro where genero = ?
    @Query(" select l from Livro l where l.genero = :genero order by l.preco ")
    List<Livro> findByGenero(
            @Param("genero") GeneroLivro generoLivro
    );

    // positional parameters
    // select * from livro where genero = ?
    @Query(" select l from Livro l where l.genero = ?1 order by l.preco ")
    List<Livro> findByGeneroPositionalParameters(
            @Param("genero") GeneroLivro generoLivro
    );

    // JPQL para escrita (update, delete)

    @Modifying // indica que a query é de modificação (insert, update, delete) e pode modificar registros
    @Transactional // indica que a query é transacional, ou seja, deve ser executada dentro de uma transação do banco
    @Query(" delete from Livro where genero = ?1 ")
    void deleteByGenero(GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query(" update Livro set dataPublicacao = ?1 ")
    void atualizarDataDePublicacaoDeLivros(LocalDate data);
}
