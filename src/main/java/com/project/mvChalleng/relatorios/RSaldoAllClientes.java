package com.project.mvChalleng.relatorios;
import lombok.Data;
import org.hibernate.dialect.OracleTypes;

import java.math.BigDecimal;
import java.sql.*;

@Data
public class RSaldoAllClientes {

    private String clienteNome;
    private Date clienteDataCriacao;
    private BigDecimal saldoAtual;

    public  void relatorio() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "math";
        String password = "123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Chama a procedure
            try (CallableStatement callableStatement = connection.prepareCall("{call relatorio_saldo_todos_clientes(?)}")) {
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                    while (resultSet.next()) {
                        // Processa os resultados
                        this.clienteNome = resultSet.getString("cliente_nome");
                        this.clienteDataCriacao = resultSet.getDate("cliente_data_criacao");
                        this.saldoAtual = resultSet.getBigDecimal("saldo_atual");

                        // Faça o que precisar com as informações
                        System.out.println("Cliente: " + this.clienteNome +
                                " - Cliente desde: " + this.clienteDataCriacao +
                                " - Saldo atual: " + this.saldoAtual);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}