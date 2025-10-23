package com.example.cursoudemy.libraryapi.repository; // Define o pacote do repositório

        import com.example.cursoudemy.libraryapi.models.Autor; // Importa a classe Autor
        import com.example.cursoudemy.libraryapi.models.GeneroLivro; // Importa o enum GeneroLivro
        import com.example.cursoudemy.libraryapi.models.Livro; // Importa a classe Livro
        import org.antlr.v4.runtime.misc.LogManager; // Importação não utilizada (pode ser removida)
        import org.junit.jupiter.api.Test; // Importa a anotação de teste do JUnit
        import org.junit.jupiter.api.TestTemplate; // Importação não utilizada (pode ser removida)
        import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação para injeção de dependências
        import org.springframework.boot.test.context.SpringBootTest; // Importa a anotação para testes Spring Boot
        import org.springframework.transaction.annotation.Transactional; // Importa a anotação para transações

        import java.math.BigDecimal; // Importa BigDecimal para valores monetários
        import java.time.LocalDate; // Importa LocalDate para datas
        import java.util.List; // Importa List para listas
        import java.util.UUID; // Importa UUID para identificadores únicos

        /**
         * @see LivroRepositoryTest
         */

        // Classe de teste para o repositório de Livro
        @SpringBootTest // Indica que o teste roda com o contexto do Spring Boot
        public class LivroRepositoryTest {
            @Autowired // Injeta o repositório de Livro
            LivroRepository repository;

            @Autowired // Injeta o repositório de Autor
            AutorRepository autores;

            @Test // Testa o método de salvar um Livro
            public void salvarTest() {
                Livro livro = new Livro(); // Cria um novo livro
                livro.setIsbn("978-85-85940-00-1"); // Define o ISBN
                livro.setTitulo("Java 100"); // Define o título
                livro.setPreco(BigDecimal.valueOf(139)); // Define o preço
                livro.setGenero(GeneroLivro.FICCAO); // Define o gênero
                livro.setDataPublicacao(LocalDate.of(2054, 10,05)); // Define a data de publicação

                Autor autor = autores.findById(UUID.fromString("9897bbfa-8d2a-41bd-bfb1-7d588c82a83d"))
                        .orElse(null); // Busca o autor pelo UUID

                livro.setAutor(autor); // Associa o autor ao livro

                repository.save(livro); // Salva o livro no banco de dados
            }

            @Test // Teste para atualizar o autor de um livro existente
            void atualizarAutorDoLivroTest() {
                UUID id = UUID.fromString("f78fda87-d813-4069-8488-8f3a31721986"); // ID do livro a ser atualizado
                var livroParaAtualizar = repository.findById(id).orElse(null); // Busca o livro pelo ID

                UUID autorId = UUID.fromString("be80cafb-0c57-4ca9-a279-faf0695490f1"); // ID do novo autor
                Autor autor = autores.findById(autorId).orElse(null); // Busca o autor pelo ID

                livroParaAtualizar.setAutor(autor); // Atualiza o autor do livro
                livroParaAtualizar.setPreco(BigDecimal.valueOf(150.0)); // Atualiza o preço

                repository.save(livroParaAtualizar); // Salva as alterações
            }

            @Test // Teste para deletar um livro pelo ID
            void deletarTest() {
                UUID id = UUID.fromString("ef7744b4-a609-435b-b1fd-20d738726c6e"); // ID do livro a ser deletado
                repository.deleteById(id); // Remove o livro do banco de dados
            }

            @Test
            @Transactional // Garante que a busca ocorra dentro de uma transação
            void buscarLivroTest() {
                UUID id = UUID.fromString("6c1cc25e-1ecc-4c89-b093-0512d3f68eaa"); // ID do livro a ser buscado
                Livro livro = repository.findById(id).orElse(null); // Busca o livro pelo ID
                System.out.println("Livro: "); // Exibe o texto "Livro:"
                System.out.println(livro.getTitulo()); // Exibe o título do livro
                System.out.println("Autor: "); // Exibe o texto "Autor:"
                System.out.println(livro.getAutor().getNome()); // Exibe o nome do autor
            }

            @Test // Teste para buscar livros por título
            void pesquisaPorTituloTest() {
                List<Livro> lista = repository.findByTitulo("Livro do games: Ato 2"); // Busca livros pelo título
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para buscar livros por ISBN
            void pesquisaPorISBNTest() {
                List<Livro> lista = repository.findByIsbn("285-81-94830-00-1"); // Busca livros pelo ISBN
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para buscar livros por título e preço
            void pesquisaPorTituloEPrecoTest() {
                List<Livro> lista = repository.findByTituloAndPreco("Livro do games: Ato 2", BigDecimal.valueOf(150)); // Busca por título e preço
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para listar todos os livros ordenados por título e preço usando JPQL
            void listarLivrosComQueryJPQLTest() {
                List<Livro> lista = repository.listarTodosOrdenadoPorTituloAndPreco(); // Busca todos os livros ordenados
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para listar autores dos livros
            void listarAutoresDosLivrosTest() {
                List<Autor> lista = repository.listarAutoresDosLivros(); // Busca autores dos livros
                lista.forEach(System.out::println); // Exibe cada autor encontrado
            }

            @Test // Teste para listar títulos de livros não repetidos
            void listarTitulosNaoRepetidosTest() {
                List<String> lista = repository.listarNomesDiferentesLivros(); // Busca títulos distintos
                lista.forEach(System.out::println); // Exibe cada título encontrado
            }

            @Test // Teste para listar livros de autores britânicos
            void listarLivrosDeAutoresBritanicos() {
                List<Livro> lista = repository.listarLivrosAtoresBritanicos(); // Busca livros de autores britânicos
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para listar livros por gênero usando parâmetro nomeado
            void listarPorGeneroQueryParam() {
                List<Livro> lista = repository.findByGenero(GeneroLivro.FICCAO); // Busca livros pelo gênero
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para listar livros por gênero usando parâmetro posicional
            void listarPorGeneroPositionalParam() {
                List<Livro> lista = repository.findByGeneroPositionalParameters(GeneroLivro.FICCAO); // Busca livros pelo gênero
                lista.forEach(System.out::println); // Exibe cada livro encontrado
            }

            @Test // Teste para deletar livros por gênero
            void deletePorGeneroTest() {
                repository.deleteByGenero(GeneroLivro.CIÊNCIA); // Deleta livros do gênero CIÊNCIA
            }

            @Test // Teste para atualizar a data de publicação de todos os livros
            void updateDataPublicacaoTest() {
                repository.atualizarDataDePublicacaoDeLivros(LocalDate.of(2020, 03, 12)); // Atualiza a data de publicação
            }
        }