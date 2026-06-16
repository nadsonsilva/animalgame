# Frontend - AnimalGame

Frontend do sistema AnimalGame desenvolvido em Angular.

A aplicação permite que usuários realizem cadastro, login, depósitos de saldo fictício, registro de apostas, simulação de sorteios e consulta ao histórico de apostas.

## Tecnologias utilizadas

* Angular 21.1.5
* TypeScript
* HTML5
* CSS3

## Funcionalidades

* Cadastro de usuário
* Login com autenticação JWT
* Visualização de saldo
* Depósito de saldo fictício
* Registro de apostas
* Tabela dos 25 grupos do jogo do bicho
* Exibição das dezenas correspondentes a cada grupo
* Simulação de sorteio
* Exibição dos 5 prêmios sorteados
* Processamento das apostas pendentes
* Histórico de apostas do usuário
* Dashboard completo

## Modalidades de aposta

O sistema suporta as seguintes modalidades:

* Grupo
* Dezena
* Centena
* Milhar
* Duque de dezena

## Como executar o frontend

Entre na pasta do projeto:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Execute a aplicação:

```bash
npm start
```

ou

```bash
ng serve
```

Após iniciar, acesse:

```text
http://localhost:4200
```

## Integração com o backend

O frontend está configurado para consumir a API do backend em:

```text
http://localhost:8080
```

Antes de iniciar o frontend, certifique-se de que o backend esteja em execução.

## Como iniciar o backend

Entre na pasta `backend`:

```bash
cd backend
```

Execute:

```bash
./mvnw clean spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean spring-boot:run
```

O parâmetro `clean` remove arquivos compilados antigos da pasta `target`, evitando problemas de compilação causados por cache do Maven.

## Fluxo de utilização

1. Cadastre um usuário.
2. Faça login.
3. Realize um depósito de saldo fictício.
4. Registre uma ou mais apostas.
5. As apostas ficam com status `PENDENTE`.
6. Execute a simulação de sorteio.
7. O sistema gera 5 milhares.
8. As apostas pendentes do usuário logado são processadas.
9. O resultado é exibido na tela.
10. O histórico e o saldo são atualizados automaticamente.

## Build para produção

Para gerar a versão de produção:

```bash
ng build
```

Os arquivos compilados serão gerados na pasta:

```text
dist/
```

## Testes

Para executar os testes do frontend:

```bash
ng test
```

## Observações

* O frontend utiliza autenticação baseada em JWT.
* O sistema utiliza saldo fictício para fins acadêmicos.
* O frontend foi desenvolvido para integração com Spring Boot 2.7.18.
* O projeto mantém as versões originais das tecnologias definidas inicialmente.
* Recomenda-se iniciar primeiro o banco de dados, depois o backend e por último o frontend.
