package com.example.cursoudemy.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration // Indica que esta classe contém configurações do Spring
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}") // Injeta a URL do banco de dados do application.properties
    String url;
    @Value("${spring.datasource.username}") // Injeta o usuário do banco
    String username;
    @Value("${spring.datasource.password}") // Injeta a senha do banco
    String password;
    @Value("${spring.datasource.driver-class-name}") // Injeta o driver JDBC
    String driver;

    //@Bean // Define um bean do tipo DataSource usando DriverManagerDataSource (padrão simples)
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource(); // Cria o DataSource simples
        ds.setUrl(url); // Configura a URL do banco
        ds.setUsername(username); // Configura o usuário
        ds.setPassword(password); // Configura a senha
        ds.setDriverClassName(driver); // Configura o driver JDBC
        return ds; // Retorna o DataSource configurado
    }

    //@Bean // Define um bean do tipo DataSource usando HikariCP (pool de conexões), que é o mais utilizado em aplicações Spring e o padrão do Spring Boot
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig(); // Cria a configuração do HikariCP
        config.setUsername(username); // Configura o usuário
        config.setPassword(password); // Configura a senha
        config.setJdbcUrl(url); // Configura a URL do banco
        config.setDriverClassName(driver); // Configura o driver JDBC

        config.setMaximumPoolSize(10); // Máximo de conexões no pool
        config.setMinimumIdle(1); // Mínimo de conexões ociosas
        config.setConnectionTimeout(100000); // Tempo máximo de espera por uma conexão (ms)
        config.setMaxLifetime(600000); // Tempo máximo de vida de uma conexão (ms)
        config.setPoolName("library-db-pool"); // Nome do pool de conexões
        config.setConnectionTestQuery("SELECT 1"); // Query para testar conexões

        return new HikariDataSource(config); // Retorna o DataSource com pool de conexões
    }
}