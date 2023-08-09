package com.example;


import java.sql.SQLException;

import com.example.dao.ConnectionManager;
import com.example.dao.DAO;
import com.example.dao.EstadoDAO;
import com.example.dao.ProdutoDAO;
import com.example.model.Marca;
import com.example.model.Produto;

public class AppBd {
    public static void main(String[] args) {
        new AppBd();
    }

    public AppBd () {
        try (var conn = ConnectionManager.getConnection()){
            var estadoDAO = new EstadoDAO(conn);
            estadoDAO.listar();
            estadoDAO.localizar("TO");            

            var marca = new Marca();
            marca.setId(1L);

            var produto =  new Produto();
            produto.setMarca(marca);
            produto.setValor(100);
            produto.setNome("Produto Teste");

            var produtoDAO = new ProdutoDAO(conn);
            produtoDAO.inserir(produto);
            produtoDAO.excluir(206);

            var dao = new DAO(conn);
            dao.listar("produto");    
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados: " + e.getMessage());
        }

    }

  

    
    private void carregarDriverJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível acessar a biblioteca para acesso ao banco de dados :" + e.getMessage());
        }
    }
    
}
