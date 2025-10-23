package com.example.cursoudemy.libraryapi.models;

import jakarta.persistence.*; // Importa as anotações JPA para mapeamento ORM
import lombok.Data; // Lombok: gera getters, setters, equals, hashCode e toString automaticamente
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate; // Representa datas sem horário
import java.util.UUID; // Identificador único universal

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "livro") // Mapeia para a tabela 'livro' no banco de dados
@Data // Lombok: gera métodos utilitários para todos os campos
@ToString(exclude = "autor")
public class Livro {
    @Id // Indica o campo como chave primária
    @Column(name = "id") // Mapeia para a coluna 'id'
    @GeneratedValue(strategy = GenerationType.UUID) // Gera o valor automaticamente como UUID
    private UUID id; // Identificador único do livro

    @Column(name = "isbn", length = 20, nullable = false) // Mapeia para a coluna 'isbn', obrigatório, até 20 caracteres
    private String isbn; // Código ISBN do livro

    @Column(name = "titulo", length = 200, nullable = false) // Mapeia para a coluna 'titulo', obrigatório, até 200 caracteres
    private String titulo; // Título do livro

    @Column(name = "data_publicacao") // Mapeia para a coluna 'data_publicacao'
    private LocalDate dataPublicacao; // Data de publicação do livro

    @Enumerated(EnumType.STRING) // Define que o campo será armazenado como String no banco, poderia ser como ORDINAL guardando a posição dos Enuns, mas é melhor usar STRING para evitar problemas com a ordem dos enums
    @Column(name = "genero", length = 100, nullable = false) // Mapeia para a coluna 'genero', obrigatório, até 100 caracteres
    private GeneroLivro genero; // Gênero do livro

    @Column(name = "preco", precision = 18, scale = 2) // Mapeia para a coluna 'preco', define precisão e casas decimais
    private BigDecimal preco; // Preço do livro

    @ManyToOne (
            //cascade = CascadeType.ALL
            // fetch = FetchType.EAGER // Carrega o autor junto com o livro (padrão para ManyToOne)
            fetch = FetchType.LAZY // Carrega o autor somente quando for acessado (padrão para OneToMany, mas não para ManyToOne)
    ) // Define relacionamento muitos-para-um com Autor
    @JoinColumn(name = "id_autor") // Mapeia para a coluna 'autor' (chave estrangeira)
    private Autor autor; // Referência ao autor do livro


}

// O atributo 'cascade' permite definir operações em cascata entre entidades relacionadas.
// Por exemplo, CascadeType.ALL faz com que operações como persistir, remover ou atualizar um Livro
// também sejam aplicadas automaticamente ao Autor relacionado.
// Outros tipos de cascade incluem: PERSIST, MERGE, REMOVE, REFRESH e DETACH.
// Usar cascade é útil para garantir integridade e facilitar o gerenciamento das entidades associadas.
// No entanto, é preciso cuidado ao usar CascadeType.ALL em relacionamentos ManyToOne,
// pois pode causar remoção ou alteração indesejada do Autor quando manipular um Livro.