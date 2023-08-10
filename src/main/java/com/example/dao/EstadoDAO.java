package com.example.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class EstadoDAO extends DAO {
  
    public EstadoDAO(Connection conn) {
        super(conn);
    }

    public void listar() {
        try{
            var statement = conn.createStatement();
            var result = statement.executeQuery("select * from estado");
            while(result.next()){
                System.out.printf("Id: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível executar a consulta ao banco: " + e.getMessage());
        }
    }

    public void localizar(String uf) {
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
}
