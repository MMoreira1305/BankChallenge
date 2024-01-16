=========== DESAFIO MV ==========

1) Resume: 
Projeto feito no intuito de ser uma API, que posteriormente ser consumida pelo front end.
A escolha de ser uma API foi por ser uma prática comum e que venho estudado esses últimos tempos,
querendo sempre aprimorar minhas habilidades.

A API se baseia em 3 Requests Mapping com "3 Rotas" (Cliente, Endereco e Conta)
- CRUD feito para todas e funcional.

Dividir meu projeto com pacotes de Repositório, Serviço, Controller, Model e Relatório
Onde o repositório era o responsável por fazer a comunicação com o banco de dados, o serviço tem a função de
fazer o tratamento dos dados passados pelo controller tratando exceções e descrepâncias em alguns atributos.
O controller fiz minhas rotas onde apenas chamei os métodos do SERVICE.

Já em relatórios fiz a comunicação manual com o banco de dados, retornando 4 procedures dos relatórios.
Fiz 4 classes diferentes, pois em cada uma posso posteriormente retornar como um DTO em alguma rota.

Por fim, o que mais consumiu meu tempo fazendo esse desafio foi a criação dos procedures e chamar
para as classes de relatórios, onde ainda não terminei o curso sobre Oracle PL/SQL mas pude dar o meu melhor para
completar este desafio.

2) Good habits
Para ter bons hábitos na hora de fazer meu código, decidir usar nomes de métodos mais fáceis de ser entendidos,
optei também por usar variáveis com nomes mais fáceis de ser entendidos novamente, também dividi meu código
em pacotes para não ficar um amontoado de coisas em uma só classe ou pacote. Além disso, tentei ao máximo evitar o
acoplamento alto, decidindo assim que cada Classe Modelo tenha sua autonomia, mesmo sendo difícil com tantos relacionamentos.

3) Design Patterns
Fiz com base no modelo Command, onde dividir meu projeto com diversos serviços, modelos, repositórios e controles.
Em um resumo básico em prático, o Chef é o Repositório, o Cozinheiro é o Serviço e o Garçom seria o Controle entregando
a informação e levando a informação de volta. Foi o motivo de ter dividido em 3 Controllers e não apenas em 1 e com
os serviços para cada modelo, e cada repositório para cada modelo também. Também utilizei o Singleton, onde consigo
instanciar uma classe de modo global por exemplo para todo o Service, onde eu fiz o @Autowired Repositório repositório.
Apenas faltando o "static" que não foi necessário pois eu não instanciei em outro lugar do código.

4) Procedures

- Os procedures fiz 2 por base de cursores e 2 com outra arquitetura de SELECT fazendo OUT em variáveis
pois, em 2 relatórios ví que os cursores foram melhores para completar o desafio, também mudando
a forma de tratar os dados em java.

Os códigos dos procedures:

-- Relatório de clientes e movimentacoes por período
CREATE OR REPLACE PROCEDURE relatorio_por_periodo(
    p_cliente_id IN NUMBER,
    p_data_inicio IN DATE,
    p_data_fim IN DATE,
    p_saldo_inicial OUT NUMBER,
    p_saldo_atual OUT NUMBER,
    p_movimentacoes_credito OUT NUMBER,
    p_movimentacoes_debito OUT NUMBER,
    p_total_movimentacoes OUT NUMBER,
    p_valor_pago OUT NUMBER
)
AS
BEGIN
    -- Cálculo do saldo inicial
    SELECT NVL(SUM(CASE WHEN tipo_movimento = 'E' AND credito_Ou_Debito = 'C' THEN valor ELSE 0 END), 0)
    INTO p_saldo_inicial
    FROM movimento
    WHERE cliente_id = p_cliente_id
    AND data_movimentacao < p_data_inicio;

    -- Cálculo do saldo atual
    SELECT NVL(SUM(CASE WHEN tipo_movimento = 'E' AND credito_ou_debito = 'C' THEN valor
                        WHEN tipo_movimento = 'S' AND credito_ou_debito = 'D' THEN -valor ELSE 0 END), 0)
    INTO p_saldo_atual
    FROM movimento
    WHERE cliente_id = p_cliente_id
    AND data_movimentacao <= p_data_fim;

    -- Movimentações de crédito
    SELECT NVL(COUNT(*), 0)
    INTO p_movimentacoes_credito
    FROM movimento
    WHERE cliente_id = p_cliente_id
    AND tipo_movimento = 'E'
    AND credito_Ou_Debito = 'C'
    AND data_movimentacao BETWEEN p_data_inicio AND p_data_fim;

    -- Movimentações de débito
    SELECT NVL(COUNT(*), 0)
    INTO p_movimentacoes_debito
    FROM movimento
    WHERE cliente_id = p_cliente_id
    AND tipo_movimento = 'S'
    AND credito_Ou_Debito = 'D'
    AND data_movimentacao BETWEEN p_data_inicio AND p_data_fim;



    -- Total de movimentações
    p_total_movimentacoes := p_movimentacoes_credito + p_movimentacoes_debito;

    SELECT NVL(SUM(CASE WHEN m.cliente_id = p_cliente_id AND m.data_movimentacao BETWEEN
    p_data_inicio AND p_data_fim THEN m.valor_Pago_Movimentacoes ELSE 0 END), 0) INTO p_valor_pago
    FROM movimento m;

END relatorio_por_periodo;

-- Relatório de Saldo por cliente
CREATE OR REPLACE PROCEDURE relatorio_saldo_cliente(
    p_cliente_id IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
    SELECT
        c.nome AS cliente_nome,
        c.data_criacao AS cliente_data_criacao,
        e.logradouro AS endereco_logradouro,
        e.numero AS endereco_numero,
        e.complemento AS endereco_complemento,
        e.bairro AS endereco_bairro,
        e.cidade AS endereco_cidade,
        e.uf AS endereco_uf,
        e.cep AS endereco_cep,
        COALESCE(SUM(CASE WHEN m.tipo_movimento = 'E' AND m.credito_ou_debito = 'C' THEN 1 ELSE 0 END), 0) AS mov_credito,
        COALESCE(SUM(CASE WHEN m.tipo_movimento = 'S' AND m.credito_ou_debito = 'S' THEN 1 ELSE 0 END), 0) AS mov_debito,
        COUNT(m.id) AS total_movimentacoes,
        COALESCE(SUM(CASE WHEN m.tipo_movimento = 'E' THEN m.valor ELSE 0 END), 0) AS valor_credito,
        COALESCE(SUM(CASE WHEN m.tipo_movimento = 'S' THEN m.valor ELSE 0 END), 0) AS valor_debito,
        COALESCE(SUM(m.valor_pago_movimentacoes), 0) AS valor_pago_movimentacoes,
        COALESCE(SUM(ct.valor_conta), 0) AS saldo_inicial,
        COALESCE(SUM(ct.valor_conta) + SUM(CASE WHEN m.tipo_movimento = 'E' THEN m.valor ELSE -m.valor END), 0) AS saldo_atual
    FROM cliente c
    LEFT JOIN endereco e ON c.id = e.cliente_id
    LEFT JOIN movimento m ON c.id = m.cliente_id
    LEFT JOIN conta ct ON c.id = ct.cliente_id
    WHERE c.id = p_cliente_id
    GROUP BY
        c.nome,
        c.data_criacao,
        e.logradouro,
        e.numero,
        e.complemento,
        e.bairro,
        e.cidade,
        e.uf,
        e.cep;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Cliente não encontrado.');
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Erro ao executar a procedure.');
END;

-- Relatório de saldo de todos os clientes
CREATE OR REPLACE PROCEDURE relatorio_saldo_todos_clientes(
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
    SELECT
        c.nome AS cliente_nome,
        c.data_criacao AS cliente_data_criacao,
        COALESCE(SUM(ct.valor_conta), 0) AS saldo_atual
    FROM cliente c
    LEFT JOIN conta ct ON c.id = ct.cliente_id
    GROUP BY
        c.nome,
        c.data_criacao;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Nenhum cliente encontrado.');
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Erro ao executar a procedure.');
END;

-- Relatório da receita da empresa
CREATE OR REPLACE PROCEDURE relatorio_receita_empresa(
    p_data_inicio IN DATE,
    p_data_fim IN DATE,
    p_cursor OUT SYS_REFCURSOR,
    p_total_receitas OUT NUMBER
) AS
BEGIN
    OPEN p_cursor FOR
    SELECT
        c.nome AS cliente_nome,
        COUNT(m.id) AS qtd_movimentacoes,
        COALESCE(SUM(m.valor), 0) AS valor_movimentacoes,
        COALESCE(Sum(x.lucro_empresa), 0) AS total_receitas
    FROM cliente c
    LEFT JOIN movimento m ON c.id = m.cliente_id,
    xpto x
    WHERE m.data_movimentacao BETWEEN p_data_inicio AND p_data_fim
    GROUP BY
        c.nome;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Nenhum cliente encontrado.');
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Erro ao executar a procedure.');
END;