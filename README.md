## Parte do Curso: Spring Boot Expert: JPA, REST, JWT, OAuth2 com Docker e AWS
Aqui estão apresentados os códigos usados durante a realização do curso da plataforma Udemy com nome mencionado acerca de uma sessão: Acesso a Dados com Spring JPA.

<hr></hr>

## Arquitetura Spring no Projeto

Este projeto utiliza a arquitetura do Spring Framework para organizar o código em camadas bem definidas, facilitando o desenvolvimento, manutenção e testes. As principais camadas presentes nas pastas libraryapi e relacionadas são focadas em JPA e acesso a dados, promovendo separação de responsabilidades e modularidade.

## 1. Entity
Modelos de dados que representam as tabelas do banco. São anotados com @Entity e possuem atributos que refletem as colunas das tabelas, com relacionamentos como @OneToMany e @ManyToOne. Exemplos: Autor, Livro e GeneroLivro.

## 2. Repository
Camada de acesso a dados, responsável por operações de CRUD no banco. Utiliza interfaces que estendem JpaRepository, permitindo que o Spring Data gere automaticamente os métodos de persistência e consultas customizadas com @Query. Exemplos: AutorRepository e LivroRepository.

## 3. Service
Contém a lógica de negócio da aplicação. Os serviços recebem dados, processam regras e interagem com os repositórios, utilizando @Transactional para gerenciar transações. Exemplos: TransacaoService.

## 4. Config
Camada de configurações, incluindo classes anotadas com @Configuration para definir beans personalizados, como DataSource com HikariCP ou DriverManagerDataSource. Exemplos: DatabaseConfiguration.

## 5. Validação e Tratamento de Erros
O projeto pode conter validações customizadas para garantir regras de negócio, como evitar duplicidade de registros. Exceções são lançadas e tratadas para garantir respostas adequadas, especialmente em transações e operações JPA.

## 6. Configurações
O Spring Boot permite configurar diversos aspectos da aplicação por meio de arquivos como application.yml, além de classes Java anotadas com @Configuration. Nessas configurações, é possível definir parâmetros de conexão com banco de dados, portas, perfis de ambiente, beans customizados e integrações externas.

### Exemplo de configurações comuns:

* application.yml: Define propriedades como URL do banco (ex.: PostgreSQL), usuário, senha, JPA settings (show-sql, ddl-auto), etc.
* Classes com @Configuration: Permitem criar beans personalizados, como DataSource, e configurar aspectos como encoding e compilação.

Essas configurações tornam o projeto flexível e adaptável a diferentes ambientes e necessidades.

<hr></hr> Essa arquitetura segue o padrão de camadas para acesso a dados com JPA, promovendo separação de responsabilidades e reutilização de código. Cada pasta (models, repository, service, config) possui seus próprios componentes, tornando o projeto modular e organizado, com foco em operações de persistência e transações.

## Como Executar
- **Pela IDE (IntelliJ)**: Execute a classe principal `Application.java` anotada com `@SpringBootApplication`.
- **Pela Linha de Comando (Windows)**:
    - Construir: `mvn clean package`
    - Rodar: `mvn spring-boot:run`
- **Executar Testes**: `mvn test`

## Configurações Úteis (Exemplo em `src/main/resources/application.yml`)
- `spring.datasource.url=jdbc:postgresql://localhost:5432/library` (para PostgreSQL via Docker).
- `spring.jpa.show-sql=true`
- `spring.jpa.properties.hibernate.format_sql=true`
- `spring.jpa.hibernate.ddl-auto=update` (ou `create-drop` para testes).

## Configuração com Docker
- **Criar Rede Docker**: `docker network create library-network`
- **Rodar PostgreSQL**: `docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library --network library-network postgres:16.3`
- **Rodar pgAdmin**: `docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin dpage/pgadmin4:8.9`

## Visão Geral dos Conceitos e Termos Importantes do JPA
- **Entidade (`@Entity`)**: Representa uma tabela; `@Id` e `@GeneratedValue` definem a chave primária.
- **Mapeamentos de Relacionamento**: `@OneToMany`, `@ManyToOne`, `fetch = FetchType.LAZY` vs `EAGER`.
- **Estados de Entidade**: Transient, Managed, Detached, Removed.
- **EntityManager e Operações**: `persist`, `merge`, `remove`, `flush`.
- **Transações (`@Transactional`)**: Operações dentro de transação para persistência.
- **Consultas**: Query Methods, `@Query` para JPQL.

## Notas Específicas para Estudo (Incluindo `atualizacaoSemAtualizar`)
- Método demonstrativo em TransacaoService para mostrar alterações em entidades managed vs detached.
- Boas práticas: Manter transações claras, usar `@Modifying @Query` para updates em massa.

## Dicas para Estudo e Experimentação
- Ative `spring.jpa.show-sql=true` e observe SQLs gerados.
- Experimente updates em managed/detached, deletes em cascata.
- Crie testes com H2.

## Recursos Adicionais
- Documentação Spring Data JPA: https://spring.io/projects/spring-data-jpa
- JPA/Hibernate Guides.

## Feito por:
### Danilo Ribeiro
### Linkedin: https://www.linkedin.com/in/danilo-ribeiro-catroli-da-silva/
### Email: danilo051007@gmail.com
