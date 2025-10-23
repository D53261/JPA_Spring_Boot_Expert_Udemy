package com.example.cursoudemy.libraryapi.models;

import jakarta.persistence.*; // Importa as anotações JPA para mapeamento ORM
import lombok.Getter; // Gera automaticamente os métodos getter
import lombok.Setter; // Gera automaticamente os métodos setter
import lombok.ToString;

import java.time.LocalDate; // Representa datas sem horário
import java.util.List;
import java.util.UUID; // Identificador único universal

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "autor", schema = "public") // Mapeia para a tabela 'autor' no schema 'public'
@Getter // Lombok: gera os getters para todos os campos
@Setter // Lombok: gera os setters para todos os campos
@ToString(exclude = "livros") // Lombok: gera o método toString para a classe poder ser impressa como string
public class Autor {

    @Id // Indica o campo como chave primária
    @Column(name = "id") // Mapeia para a coluna 'id'
    @GeneratedValue(strategy = GenerationType.UUID) // Gera o valor automaticamente como UUID
    // Existem outros GeneratedValue, como AUTO, IDENTITY, SEQUENCE, TABLE, mas UUID é o mais adequado para identificadores únicos
    // AUTO: O JPA escolhe a estratégia de geração de ID mais adequada para o banco de dados
    // IDENTITY: O banco de dados gera o ID automaticamente (usado principalmente em bancos como MySQL e PostgreSQL)
    // SEQUENCE: Usa uma sequência do banco de dados para gerar IDs (comum em bancos como Oracle e PostgreSQL)
    // TABLE: Usa uma tabela auxiliar para gerar IDs (menos comum, mas útil em alguns casos)

    private UUID id; // Identificador único do autor

    @Column(name = "nome", length = 100, nullable = false) // Mapeia para a coluna 'nome', obrigatório, até 100 caracteres
    private String nome; // Nome do autor

    @Column(name = "data_nascimento", nullable = false) // Mapeia para a coluna 'data_nascimento', obrigatório
    private LocalDate dataNascimento; // Data de nascimento do autor

    @Column(name = "nacionalidade", length = 50, nullable = false) // Mapeia para a coluna 'nacionalidade', obrigatório, até 50 caracteres
    private String nacionalidade; // Nacionalidade do autor

    @OneToMany(mappedBy = "id_autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Relacionamento um-para-muitos com a entidade Livro, pegando o nome da coluna que referencia o autor na tabela Livro, com operações em cascata e carregamento imediato
    @Transient // Indica que este campo não será persistido no banco de dados
    private List<Livro> livros; // Lista de livros escritos pelo autor

}