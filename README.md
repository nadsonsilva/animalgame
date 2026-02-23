# 🐾 **Jogo do Bicho – AnimalGame**

A **AnimalGame** é uma aplicação web desenvolvida para simular o tradicional **Jogo do Bicho**, com fins exclusivamente educacionais.

O projeto está sendo desenvolvido como parte da disciplina **Laboratório de Produção de Software**, ministrada pelo professor **Ronem Lavareda** no **IFAM – Campus Parintins**.

⚠️ **Este sistema tem caráter acadêmico e não possui qualquer vínculo com práticas reais de apostas.**

🚧 **Status do Projeto**  
🚀 Em desenvolvimento.  
Atualmente, a aplicação está em fase de implementação e evolução contínua, com melhorias sendo adicionadas gradualmente tanto no **front-end** quanto no **back-end**.

---

## 🛠️ **Tecnologias Utilizadas**

### 🎨 **Front-end**
- **Angular**: Framework para construção da interface de usuário.
- **Bootstrap**: Framework CSS para responsividade e design.

### ⚙️ **Back-end**
- **Java**: Linguagem principal para o desenvolvimento do backend.
- **Spring Boot**: Framework para criação da API RESTful, facilitando a comunicação entre o backend e o frontend.

### 🗄️ **Banco de Dados**
- **MySQL**: Banco de dados relacional utilizado para persistência de dados.
- **Docker**: Utilizado para containerizar o banco de dados MySQL e garantir ambientes de desenvolvimento consistentes.

### 🔐 **Segurança**
- **JWT (JSON Web Token)**: Implementação de autenticação e autorização para proteger as rotas da API.

### 📦 **Testes**
- **Cypress**: Framework de testes E2E (End-to-End), utilizado para garantir que o frontend e o backend funcionem corretamente juntos.

### 🤖 **Automação**
- **GitHub Actions**: Ferramenta para integrar e automatizar o processo de **build**, **testes** e **deploy** do projeto.

### 📑 **Documentação**
- **Swagger**: Utilizado para documentar a API, permitindo que os desenvolvedores interajam com a API de forma visual e intuitiva.

---

## 📌 **Arquitetura**

A aplicação segue o padrão de **arquitetura em camadas**, separando responsabilidades para melhorar a organização, escalabilidade e manutenção do sistema.

- **Controller**: Gerencia as requisições HTTP e orquestra a execução das lógicas no back-end.
- **Service**: Implementa a lógica de negócio, delegando as operações ao repositório.
- **Repository**: Responsável pela persistência de dados no banco de dados.
- **Model**: Define as entidades que representam os dados da aplicação.

### Diagrama da Arquitetura
![Diagrama da Arquitetura](Diagrama%20de%20arquitetura%20web%20e%20backend.png)
---

## 🔐 **Funcionalidades do Back-end (API)**

A **API REST** está sendo desenvolvida com os seguintes endpoints:

### 🔑 **Autenticação**
- **POST /auth/signup** → Cadastro de usuário.
- **POST /auth/signin** → Login de usuário.

### 👤 **Usuário**
- **GET /users/me** → Dados do usuário autenticado.
- **GET /users/me/bets** → Listar apostas do usuário.
- **PATCH /users/me/change_password** → Alterar senha.
- **POST /users/me/deposit** → Realizar depósito de saldo.

### 🐯 **Animais**
- **GET /animals** → Listar animais disponíveis para apostas.

### 🎲 **Apostas**
- **POST /bets** → Criar nova aposta.
- **GET /bets/{id}/result** → Obter o resultado de uma aposta.

---

## 📝 **Desenvolvido por**
**Nadson Silva** 🐾  
**IFAM – Campus Parintins**
