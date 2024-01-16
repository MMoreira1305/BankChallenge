package com.project.mvChalleng.relatorios;

import com.project.mvChalleng.model.Endereco;
import com.project.mvChalleng.repository.ClienteRepository;
import com.project.mvChalleng.repository.EnderecoRepository;
import com.project.mvChalleng.repository.MovimentoRepository;
import lombok.Data;
import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.*;

@Data
public class RSaldoCliente {
    private String nome;
    private Endereco endereco;
    private Long movCredito;
    private Long movDebito;
    private Long totalMov;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private BigDecimal valorTotalMov;


    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private MovimentoRepository movimentoRepository;
    private Date dataCriacao;

    public void relatorioSaldoCliente(int id){
        this.endereco = new Endereco();
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "math";
        String password = "123";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Chamar a stored procedure
            chamarStoredProcedure(connection, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chamarStoredProcedure(Connection connection, int id) throws SQLException {
        String call = "{call relatorio_saldo_cliente(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(call)) {

            callableStatement.setInt(1 ,id);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                while (resultSet.next()) {
                    // Processar os resultados
                    this.nome = resultSet.getString("cliente_nome");
                    this.endereco.setLogradouro(resultSet.getString("endereco_logradouro"));
                    this.endereco.setNumero(resultSet.getString("endereco_numero"));
                    this.endereco.setComplemento(resultSet.getString("endereco_complemento"));
                    this.endereco.setBairro(resultSet.getString("endereco_bairro"));
                    this.endereco.setCidade(resultSet.getString("endereco_cidade"));
                    this.endereco.setUf(resultSet.getString("endereco_uf"));
                    this.endereco.setCep(resultSet.getString("endereco_cep"));
                    this.movCredito = (long) resultSet.getInt("mov_credito");
                    this.movDebito = (long) resultSet.getInt("mov_debito");
                    this.totalMov = (long) resultSet.getInt("total_movimentacoes");
                    this.valorTotalMov = resultSet.getBigDecimal("valor_pago_movimentacoes");
                    this.saldoInicial = resultSet.getBigDecimal("saldo_inicial");
                    this.saldoAtual = resultSet.getBigDecimal("saldo_atual");
                    System.out.println("Cliente: " + this.nome);
                    System.out.println("Endereço: " + this.endereco.getLogradouro() + ", " +
                            this.endereco.getNumero() + ", " +
                            this.endereco.getComplemento() + ", " +
                            this.endereco.getBairro() + ", " +
                            this.endereco.getCidade() + ", " +
                            this.endereco.getUf() + ", " +
                            this.endereco.getCep());
                    System.out.println("Movimentações de crédito: " + this.movCredito);
                    System.out.println("Movimentações de débito: " + this.movCredito);
                    System.out.println("Total de movimentações: " + this.totalMov);
                    System.out.println("Valor pago pelas movimentações: " + this.valorTotalMov);
                    System.out.println("Saldo inicial: " + this.saldoInicial);
                    System.out.println("Saldo atual: " + this.saldoAtual);
                }
            }
        }
    }
}

