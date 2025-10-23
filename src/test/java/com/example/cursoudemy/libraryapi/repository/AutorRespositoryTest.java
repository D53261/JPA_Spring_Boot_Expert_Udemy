package com.example.cursoudemy.libraryapi.repository; // Define o pacote do repositório de testes

import com.example.cursoudemy.libraryapi.models.Autor; // Importa a entidade Autor para testes de CRUD
import com.example.cursoudemy.libraryapi.models.GeneroLivro; // Importa o enum GeneroLivro para definir gêneros em livros associados
import com.example.cursoudemy.libraryapi.models.Livro; // Importa a entidade Livro para testes de relacionamentos
import org.junit.jupiter.api.Test; // Importa a anotação @Test do JUnit 5 para definir métodos de teste
import org.springframework.beans.factory.annotation.Autowired; // Permite injeção automática de dependências do Spring
import org.springframework.boot.test.context.SpringBootTest; // Inicializa o contexto completo do Spring Boot para testes integrados
import org.springframework.transaction.annotation.Transactional; // Importa anotação para gerenciar transações em testes

import java.math.BigDecimal; // Importa BigDecimal para manipulação precisa de valores monetários
import java.time.LocalDate; // Importa LocalDate para trabalhar com datas de nascimento e publicação
import java.util.ArrayList; // Importa ArrayList para criar listas mutáveis de livros
import java.util.List; // Importa List para armazenar coleções de entidades
import java.util.Optional; // Importa Optional para representar valores que podem ou não estar presentes
import java.util.UUID; // Importa UUID para identificadores únicos de entidades

/**
 * Classe de teste para o AutorRepository, demonstrando operações CRUD em entidades Autor e seus relacionamentos com Livro.
 * Cada teste valida funcionalidades como salvar, atualizar, listar, contar e deletar autores, além de cascata em relacionamentos
 * @OneToMany. Utiliza @SpringBootTest para contexto real, permitindo testes integrados com banco de dados.
 */
@SpringBootTest // Indica que o teste roda com o contexto completo do Spring Boot, incluindo injeção de dependências e banco
public class AutorRespositoryTest { // Classe de teste para o repositório Autor

    @Autowired // Injeta o repositório de Autor para executar operações CRUD
    AutorRepository repository; // Repositório para operações com Autor

    @Autowired // Injeta o repositório de Livro para testes de relacionamentos
    LivroRepository livroRepository; // Repositório para operações com Livro

    /**
     * Teste para demonstrar a operação de salvar (persistir) um novo autor no banco de dados.
     * Cria uma instância de Autor (estado Transient), define atributos e salva.
     * Após save(), a entidade transita para Managed, e o ID é gerado automaticamente.
     */
    @Test
    public void salvarTest() { // Testa salvar um novo autor
        Autor autor = new Autor(); // Cria uma nova instância de Autor (Transient)
        autor.setNome("Teste bom"); // Define o nome do autor
        autor.setNacionalidade("Britânico"); // Define a nacionalidade
        autor.setDataNascimento(LocalDate.of(1892, 1, 3)); // Define a data de nascimento

        var autorSalvo = repository.save(autor); // Salva o autor (persiste e torna Managed)
        System.out.println("Autor salvo com ID: " + autorSalvo); // Exibe o autor salvo com ID gerado
    }

    /**
     * Teste para demonstrar a atualização de um autor existente.
     * Busca o autor pelo ID (torna-o Managed), modifica atributos e salva.
     * Mudanças em entidades Managed são detectadas automaticamente pelo JPA no commit.
     */
    @Test
    public void atualizarTest() { // Testa atualizar um autor existente
        var id = UUID.fromString("2c3a3439-f955-4940-8e82-b0d4986a64a1"); // ID do autor a ser atualizado
        Optional<Autor> autor = repository.findById(id); // Busca o autor (Managed se encontrado)
        if (autor.isPresent()) { // Verifica se o autor existe
            Autor autorEncontrado = autor.get(); // Obtém o autor encontrado
            System.out.println("Dados do autor: \n" + autorEncontrado); // Exibe os dados atuais do autor

            autorEncontrado.setDataNascimento(LocalDate.of(1892, 1, 3)); // Atualiza a data de nascimento (mudança detectada)
            repository.save(autorEncontrado); // Salva as alterações (explícito, mas automático em Managed)
        }
    }

    /**
     * Teste para demonstrar a listagem de todos os autores usando findAll().
     * Exibe todos os registros no banco, ilustrando consultas básicas do JpaRepository.
     */
    @Test
    public void listarTest() {
        List<Autor> lista = repository.findAll(); // Busca todos os autores (Managed)
        lista.forEach(System.out::println); // Exibe cada autor na lista
    }

    /**
     * Teste para demonstrar a contagem de registros usando count().
     * Exibe o total de autores no banco, útil para validações de integridade.
     */
    @Test
    public void countTest() {
        System.out.println("Total de autores: " + repository.count()); // Exibe o total de autores
    }

    /**
     * Teste para demonstrar a operação de deletar um autor pelo ID.
     * Remove a entidade do banco, ilustrando deleteById() e o estado Removed.
     * Nota: Se houver livros associados sem orphanRemoval, pode causar erros de integridade.
     */
    @Test
    public void deleteTest() {
        var id = UUID.fromString("e5193a7e-6291-46aa-a332-d41e72391156"); // ID do autor a ser deletado
        var item = repository.findById(id).get(); // Busca o autor antes de deletar (opcional para validação)
        repository.deleteById(id); // Deleta o autor (marca como Removed e remove do banco)
    }

    /**
     * Teste para demonstrar salvamento em cascata (@OneToMany com cascade = CascadeType.ALL).
     * Cria um autor com livros associados, salva o autor e os livros automaticamente.
     * Mostra como o JPA propaga operações para entidades relacionadas.
     */
    @Test
    void salvarAutorComLivrosTest() {
        Autor autor = new Autor(); // Cria uma nova instância de Autor (Transient)
        autor.setNome("Clovis de Barros Filho"); // Define o nome do autor
        autor.setNacionalidade("Britânico"); // Define a nacionalidade
        autor.setDataNascimento(LocalDate.of(1992, 1, 3)); // Define a data de nascimento

        // Cria o primeiro livro associado
        Livro livro = new Livro();
        livro.setIsbn("578-81-68090-00-1");
        livro.setTitulo("Manifesto anarquista");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIÊNCIA);
        livro.setDataPublicacao(LocalDate.of(1707, 10, 05));
        livro.setAutor(autor); // Associa o autor (bidirecional)

        // Cria o segundo livro associado
        Livro livro2 = new Livro();
        livro2.setIsbn("285-81-94830-00-1");
        livro2.setTitulo("Manifesto anarquista: Ato 2");
        livro2.setPreco(BigDecimal.valueOf(100));
        livro2.setGenero(GeneroLivro.CIÊNCIA);
        livro2.setDataPublicacao(LocalDate.of(1977, 10, 05));
        livro2.setAutor(autor); // Associa o autor

        autor.setLivros(new ArrayList<>()); // Inicializa a lista de livros no autor
        autor.getLivros().add(livro); // Adiciona o primeiro livro
        autor.getLivros().add(livro2); // Adiciona o segundo livro

        repository.save(autor); // Salva o autor (cascata salva os livros devido a @OneToMany)
        livroRepository.saveAll(autor.getLivros()); // Salva explicitamente os livros (opcional com cascata)
    }

    /**
     * Teste para demonstrar carregamento de relacionamentos lazy (@OneToMany fetch = FetchType.LAZY).
     * Busca um autor, carrega seus livros via query separada e exibe.
     * Usa @Transactional implicitamente via contexto de teste para evitar LazyInitializationException.
     */
    @Test
    void listarLivrosAutor() {
        var id = UUID.fromString("be80cafb-0c57-4ca9-a279-faf0695490f1"); // ID do autor
        var autor = repository.findById(id).get(); // Busca o autor (Managed)

        // Busca livros associados via query method (evita carregamento lazy automático)
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista); // Associa os livros ao autor (para exibição)

        autor.getLivros().forEach(System.out::println); // Exibe cada livro do autor
    }
}
