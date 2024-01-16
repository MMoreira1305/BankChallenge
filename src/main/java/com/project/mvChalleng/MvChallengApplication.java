package com.project.mvChalleng;

import com.project.mvChalleng.relatorios.RReceitaEmpresa;
import com.project.mvChalleng.relatorios.RSaldoAllClientes;
import com.project.mvChalleng.relatorios.RSaldoCliente;
import com.project.mvChalleng.relatorios.RSaldoClientePeriodo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;


@SpringBootApplication
@EnableScheduling
public class MvChallengApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvChallengApplication.class, args);

		LocalDate localDateInicial = LocalDate.of(2024, 1, 15);
		Date dateInicial = new Date(localDateInicial.atStartOfDay(ZoneId.of("GMT")).toEpochSecond() * 1000);

		LocalDate localDateFinal = LocalDate.of(2024, 1, 15);
		Date dateFinal = new Date(localDateFinal.atStartOfDay(ZoneId.of("GMT")).toEpochSecond() * 1000);


		// Relatório Saldo Cliente
		RSaldoCliente rSaldoCliente = new RSaldoCliente();
		rSaldoCliente.relatorioSaldoCliente(302);

		// Relatorio Cliente por Período
		RSaldoClientePeriodo rSaldoClientePeriodo = new RSaldoClientePeriodo();
		rSaldoClientePeriodo.chamarRelatorioSaldoCliente(302L, dateInicial, dateFinal);

		// Relatorio Saldo de todos os clientes
		RSaldoAllClientes rSaldoAllClientes = new RSaldoAllClientes();
		rSaldoAllClientes.relatorio();

		// Relatório da receita da empresa
		RReceitaEmpresa rReceitaEmpresa = new RReceitaEmpresa();
		rReceitaEmpresa.relatorio(dateInicial, dateFinal);
	}


}
