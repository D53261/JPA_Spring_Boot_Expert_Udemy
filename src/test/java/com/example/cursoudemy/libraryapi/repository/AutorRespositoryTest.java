package com.example.cursoudemy.libraryapi.repository; // Pacote do repositório

import com.example.cursoudemy.libraryapi.models.Autor; // Importa a entidade Autor
import com.example.cursoudemy.libraryapi.models.GeneroLivro;
import com.example.cursoudemy.libraryapi.models.Livro;
import org.junit.jupiter.api.Test; // Importa a anotação de teste do JUnit
import org.springframework.beans.factory.annotation.Autowired; // Permite injeção de dependência
import org.springframework.boot.test.context.SpringBootTest; // Inicializa o contexto Spring Boot para testes
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate; // Manipula datas
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Representa valor opcional (pode estar presente ou não)
import java.util.UUID; // Identificador único universal

@SpringBootTest // Indica que o teste deve rodar com o contexto do Spring Boot
public class AutorRespositoryTest { // Classe de teste para o repositório Autor

    @Autowired // Injeta automaticamente o AutorRepository
    AutorRepository repository; // Repositório para operações com Autor

    @Autowired // Injeta automaticamente o LivroRepository
    LivroRepository livroRepository; // Repositório para operações com Livro

    @Test // Indica que este método é um teste
    public void salvarTest() { // Testa salvar um novo autor
        Autor autor = new Autor(); // Cria uma nova instância de Autor
        autor.setNome("Teste bom"); // Define o nome do autor
        autor.setNacionalidade("Britânico"); // Define a nacionalidade
        autor.setDataNascimento(LocalDate.of(1892, 1, 3)); // Define a data de nascimento

        var autorSalvo = repository.save(autor); // Salva o autor no banco de dados
        System.out.println("Autor salvo com ID: " + autorSalvo); // Exibe o autor salvo
    }

    @Test // Indica que este método é um teste
    public void atualizarTest() { // Testa atualizar um autor existente
        var id = UUID.fromString("2c3a3439-f955-4940-8e82-b0d4986a64a1"); // ID do autor a ser atualizado
        Optional<Autor> autor = repository.findById(id); // Busca o autor pelo ID
        if (autor.isPresent()) { // Verifica se o autor existe
            Autor autorEncontrado = autor.get(); // Obtém o autor encontrado
            System.out.println("Dados do autor: \n" + autorEncontrado); // Exibe os dados do autor

            autorEncontrado.setDataNascimento(LocalDate.of(1892, 1, 3)); // Atualiza a data de nascimento
            repository.save(autorEncontrado); // Salva as alterações no banco
        }
    }

    @Test // Indica que este método é um teste
    public void listarTest() {
        List<Autor> lista = repository.findAll(); // Busca todos os autores no banco de dados
        lista.forEach(System.out::println); // Exibe cada autor na lista
    }

    @Test // Indica que este método é um teste
    public void countTest() {
        System.out.println("Total de autores: " + repository.count()); // Exibe o total de autores no banco de dados
    }

    @Test // Indica que este método é um teste
    public void deleteTest() {
        var id = UUID.fromString("e5193a7e-6291-46aa-a332-d41e72391156"); // ID do autor a ser deletado
        var item = repository.findById(id).get(); // Tenta buscar o autor deletado
        repository.deleteById(id); // Deleta o autor pelo ID
    }

    @Test
    void salvarAutorComLivrosTest() {
        Autor autor = new Autor(); // Cria uma nova instância de Autor
        autor.setNome("Clovis de Barros Filho"); // Define o nome do autor
        autor.setNacionalidade("Britânico"); // Define a nacionalidade
        autor.setDataNascimento(LocalDate.of(1992, 1, 3)); // Define a data de nascimento

        // Cria um novo livro e define seus atributos
        Livro livro = new Livro();
        livro.setIsbn("578-81-68090-00-1");
        livro.setTitulo("Manifesto anarquista");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIÊNCIA);
        livro.setDataPublicacao(LocalDate.of(1707, 10,05));
        livro.setAutor(autor);

        // Cria um novo livro e define seus atributos
        Livro livro2 = new Livro();
        livro2.setIsbn("285-81-94830-00-1");
        livro2.setTitulo("Manifesto anarquista: Ato 2");
        livro2.setPreco(BigDecimal.valueOf(100));
        livro2.setGenero(GeneroLivro.CIÊNCIA);
        livro2.setDataPublicacao(LocalDate.of(1977, 10,05));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        repository.save(autor);
        livroRepository.saveAll(autor.getLivros());
    }


    @Test
        void listarLivrosAutor() {
            var id = UUID.fromString("be80cafb-0c57-4ca9-a279-faf0695490f1");
            var autor = repository.findById(id).get();

            List<Livro> livrosLista = livroRepository.findByAutor(autor);
            autor.setLivros(livrosLista);

            autor.getLivros().forEach(System.out::println);
    }
}