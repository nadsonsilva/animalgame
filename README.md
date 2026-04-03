# AnimalGame

Sistema web acadêmico para simular o jogo do bicho, com autenticação JWT, cadastro de usuários, listagem de animais, registro de apostas e histórico individual.

## Tecnologias já mantidas no projeto

### Frontend
- Angular 21
- TypeScript
- HTML/CSS

### Backend
- Java 17
- Spring Boot 2.7.18
- Spring Security
- Spring Data JPA
- JWT
- Swagger

### Banco de dados
- MySQL 8
- Docker Compose

## Funcionalidades concluídas

- Cadastro de usuário
- Login com JWT
- Proteção das rotas autenticadas
- CORS configurado para integração com frontend
- Listagem dos 25 animais do jogo do bicho
- Registro de aposta com validação de saldo
- Sorteio automático ao apostar
- Cálculo de prêmio para aposta vencedora
- Atualização automática do saldo do usuário
- Histórico de apostas por usuário
- Dashboard completo no frontend
- Swagger habilitado

## Rotas existentes do backend

### Públicas
- `POST /usuarios`
- `POST /auth/login`

### Protegidas por token
- `GET /usuarios`
- `GET /usuarios/{id}`
- `GET /animais`
- `POST /apostas`
- `GET /apostas`
- `GET /apostas/historico/{usuarioId}`
- `POST /sorteio`

## Como rodar o banco

Na raiz do projeto:

```bash
docker-compose up -d
```

## Como rodar o backend

Entre na pasta `backend` e execute:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Swagger:

```text
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

1. Suba o MySQL com Docker.
2. Inicie o backend.
3. Inicie o frontend.
4. Acesse `/cadastro` e crie um usuário com saldo inicial.
5. Faça login.
6. No dashboard, escolha um grupo e registre a aposta.
7. Confira o resultado da rodada e o histórico.

## Observações

- O arquivo `data.sql` popula automaticamente os 25 animais.
- O projeto mantém as rotas principais já definidas.
- O frontend já está conectado ao backend em `http://localhost:8080`.
