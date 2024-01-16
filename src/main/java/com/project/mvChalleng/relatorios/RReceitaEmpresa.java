package com.project.mvChalleng.relatorios;
import lombok.Data;
import org.hibernate.dialect.OracleTypes;

import java.math.BigDecimal;
import java.sql.*;

@Data
public class RReceitaEmpresa {

    private String clienteNome;
    private int qtdMovimentacoes;
    private BigDecimal valorMovimentacoes;
    private BigDecimal total_receita;


    public void relatorio(Date dataInicio, Date dataFinal) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "math";
        String password = "123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Chama a procedure
            try (CallableStatement callableStatement = connection.prepareCall("{call relatorio_receita_empresa(?, ?, ?, ?)}")) {
                callableStatement.setDate(1, dataInicio);
                callableStatement.setDate(2, dataFinal);
                callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
                callableStatement.registerOutParameter(4, Types.NUMERIC);
                callableStatement.execute();

                try (ResultSet resultSet = (ResultSet) callableStatement.getObject(3)) {
                    while (resultSet.next()) {
                        // Processa os resultados
                        this.clienteNome = resultSet.getString("cliente_nome");
                        this.qtdMovimentacoes = resultSet.getInt("qtd_movimentacoes");
                        this.valorMovimentacoes = resultSet.getBigDecimal("valor_movimentacoes");
                        this.total_receita = resultSet.getBigDecimal("total_receitas");

                        System.out.println("Cliente: " + this.clienteNome +
                                " - Quantidade de movimentações: " + this.qtdMovimentacoes +
                                " - Valor das movimentações: R$ " + this.valorMovimentacoes +
                                " - Total de Receita: R$ " + this.total_receita);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
