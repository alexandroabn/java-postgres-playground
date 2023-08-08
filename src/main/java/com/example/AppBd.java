package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AppBd {
    private static final String PASSWORD = "";
    private static final String USERNAME = "gitpod";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/postgres";
    //private Connection conn;

    public static void main(String[] args) {
        new AppBd();
    }

    public AppBd () {
        try (var conn = getConnection()){
            listarEstados(conn);
            localizarEstado(conn, "TO");
            

            var marca = new Marca();
            marca.setId(1L);

            var produto =  new Produto();
            produto.setMarca(marca);
            produto.setValor(100);
            produto.setNome("Produto Teste");

            inserirProduto(conn, produto);
            excluirProduto(conn, 202);
            listarDadosTabela(conn, "produto");
            
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados: " + e.getMessage());
        }

    }


    private void excluirProduto(Connection conn, long id) {
        var sql = "delete from produto where id = ?";
        try {
            var statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir o produto: " + e.getMessage());
        }
       

    }

    private void inserirProduto(Connection conn, Produto produto) {
        var sql = "insert into produto (nome, marca_id, valor) values (?,?,?)";
        try  (var statement = conn.prepareStatement(sql)){
            statement.setString(1, produto.getNome());
            statement.setLong(2, produto.getMarca().getId());
            statement.setDouble(3, produto.getValor());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro na execução da consulta :" + e.getMessage());
        }
    }

    private void listarDadosTabela(Connection conn, String tabela) {
        var sql = "select * from " + tabela;
        try {
            var statement = conn.createStatement();
            var result = statement.executeQuery(sql);
            
            var metadata =  result.getMetaData();
            int cols =  metadata.getColumnCount();

            for (int i = 1; i < cols; i++) {
                System.out.printf("%-25s |", metadata.getColumnName(i));
            }
            System.out.println();
            
            while(result.next()){
                
                for (int i = 1; i < cols; i++) {
                    System.out.printf("%-25s |", result.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Erro na execução da consulta :" + e.getMessage());
        }
    }

    private void listarEstados(Connection conn) {
        Statement statement = null;
        try{
            System.out.println("conexão com o banco realizada com sucesso");

            statement = conn.createStatement();
            var result = statement.executeQuery("select * from estado");
            while(result.next()){
                System.out.printf("Id: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível executar a consulta ao banco: " + e.getMessage());
        }
    }

    private void localizarEstado(Connection conn, String uf) {
        try {
            var sql = "select * from estado where uf = ?";
            var statement = conn.prepareStatement(sql);
            statement.setString(1, uf);
            var result = statement.executeQuery();
            if(result.next()){
                System.out.printf("Id: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível executar a consulta ao banco: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    private void carregarDriverJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível acessar a biblioteca para acesso ao banco de dados :" + e.getMessage());
        }
    }
    
}
