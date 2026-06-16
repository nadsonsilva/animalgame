# AnimalGame

Sistema web acadêmico para simular o jogo do bicho, com autenticação JWT, cadastro de usuários, listagem de animais, registro de apostas e histórico individual.

## Tecnologias já mantidas no projeto

### Frontend

* Angular 21
* TypeScript
* HTML/CSS

### Backend

* Java 17
* Spring Boot 2.7.18
* Spring Security
* Spring Data JPA
* JWT
* Swagger

### Banco de dados

* MySQL 8
* Docker Compose

## Funcionalidades concluídas

* Cadastro de usuário
* Login com JWT
* Proteção das rotas autenticadas
* CORS configurado para integração com frontend
* Listagem dos 25 animais do jogo do bicho
* Tabela com grupos, animais e dezenas
* Registro de apostas pendentes
* Simulação de sorteio com 5 milhares
* Processamento das apostas pendentes do usuário logado
* Cálculo de premiação para apostas vencedoras
* Atualização automática do saldo após o sorteio
* Exibição dos resultados processados
* Histórico de apostas por usuário
* Dashboard completo no frontend
* Swagger habilitado

## Modalidades de aposta implementadas

* Grupo
* Dezena
* Centena
* Milhar
* Duque de dezena

## Fluxo do sorteio

Fluxo atual:

1. O usuário registra uma aposta.
2. O valor é descontado do saldo.
3. A aposta fica com status `PENDENTE`.
4. O usuário executa a simulação de sorteio.
5. O sistema gera 5 milhares.
6. As apostas pendentes do usuário logado são processadas.
7. O saldo é atualizado em caso de vitória.
8. A aposta passa para o status `FINALIZADA`.

## Premiação implementada

### Grupo na cabeça

Quando o grupo é acertado no 1º prêmio:

```
valor apostado × 18
```

### Grupo cercado

Quando o grupo é acertado entre o 2º e o 5º prêmio:

```
(valor apostado × 18) ÷ 5
```

## Rotas existentes do backend

### Públicas

* `POST /usuarios`
* `POST /auth/login`

### Protegidas por token

* `GET /usuarios`
* `GET /usuarios/{id}`
* `GET /animais`
* `POST /apostas`
* `GET /apostas`
* `GET /apostas/historico/{usuarioId}`
* `POST /sorteio`

## Como rodar o banco

Na raiz do projeto:

```bash
docker-compose up -d
```

## Como rodar o backend

Entre na pasta `backend` e execute:

```bash
./mvnw clean spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean spring-boot:run
```

> O parâmetro `clean` remove arquivos compilados antigos da pasta `target`, evitando erros causados por cache do Maven.

Swagger:

```
http://localhost:8080/swagger-ui.html
```

## Como rodar o frontend

Entre na pasta `frontend` e execute:

```bash
npm install
npm start
```

Frontend:

```text
http://localhost:4200
```

## Fluxo para testar o sistema

1. Em uma aba no terminal, Suba o MySQL com Docker.
2. Em outra aba do terminal, Inicie o backend.
3. Em outra aba do terminal, Inicie o frontend.
4. Acesse `/cadastro` e crie um usuário com saldo inicial.
5. Faça login.
6. No dashboard, escolha uma modalidade e registre a aposta.
7. A aposta ficará com status `PENDENTE`.
8. Execute a simulação de sorteio.
9. Confira os resultados processados.
10. Verifique o histórico e o saldo atualizado.

## Executando os testes

Dentro da pasta `backend`:

```bash
./mvnw clean test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean test
```

## Observações

* O arquivo `data.sql` popula automaticamente os 25 animais.
* O projeto mantém as rotas principais já definidas.
* O frontend já está conectado ao backend em `http://localhost:8080`.
* O projeto mantém as tecnologias e versões originalmente definidas.
* O sistema foi desenvolvido para fins acadêmicos e utiliza saldo fictício.
* As apostas são processadas somente após a simulação de sorteio.
* O sorteio gera 5 milhares e processa apenas as apostas pendentes do usuário logado.
* O backend utiliza Maven Wrapper (`mvnw`), dispensando instalação manual do Maven na máquina.
* Recomenda-se utilizar sempre os comandos com `clean` antes da execução para evitar inconsistências de compilação.
