# Gerenciador de Tarefas

## Resumo do Projeto

O **Gerenciador de Tarefas** é uma aplicação backend desenvolvida em **Java** com **Spring Boot** para gerenciar tarefas. Ele oferece funcionalidades como:

- Criação, atualização, visualização e exclusão de tarefas.
- Controle de status e prioridade das tarefas.
- Sistema de autenticação e autorização com Spring Security, baseado em tokens JWT.
- Diferentes permissões de usuários (Admin e Usuário Básico).
  
A aplicação utiliza o banco de dados **PostgreSQL** e implementa a arquitetura de **REST API**, com todas as operações sendo acessíveis via HTTP.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (Framework principal)
- **Spring Security** (Segurança e autenticação)
- **JWT** (Autenticação baseada em token)
- **JPA/Hibernate** (Mapeamento objeto-relacional)
- **PostgreSQL** (Banco de dados)
- **Swagger** (Documentação da API)
- **Maven** (Gerenciamento de dependências)

## Como Usar o Projeto

### 1. Pré-requisitos

- **Java 17** ou superior.
- **Maven** para gerenciamento de dependências e execução.
- **Banco de dados PostgreSQL** configurado.

### 2. Como Rodar a Aplicação

1. Clone o repositório para o seu computador:
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-de-tarefas.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd gerenciador-de-tarefas
   ```

3. Altere as configurações do banco de dados no arquivo `application.properties` para corresponder às suas configurações locais:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

4. Compile e execute a aplicação com o Maven:
   ```bash
   mvn clean spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

## Como Usar as Requisições

A API do Gerenciador de Tarefas permite as seguintes operações:

### 1. **Login**
   **POST** `/login`  
   Realiza o login do usuário, retornando um **JWT token** que será utilizado nas requisições subsequentes.

   **Exemplo de request:**
   ```json
   {
       "username": "usuario_teste",
       "password": "senha_teste"
   }
   ```

### 2. **Criar Tarefa**
   **POST** `/tasks`  
   Cria uma nova tarefa associada ao usuário logado.

   **Exemplo de request:**
   ```json
   {
       "title": "Nova Tarefa",
       "description": "Descrição da tarefa",
       "status": "PENDENTE",
       "priority": "ALTA"
   }
   ```

### 3. **Atualizar Tarefa**
   **PUT** `/tasks/{id}`  
   Atualiza uma tarefa existente.

   **Exemplo de request:**
   ```json
   {
       "title": "Tarefa Atualizada",
       "description": "Descrição atualizada",
       "status": "EM_ANDAMENTO",
       "priority": "MEDIA"
   }
   ```

### 4. **Excluir Tarefa**
   **DELETE** `/tasks/{id}`  
   Exclui uma tarefa existente.

### 5. **Obter Tarefas**
   **GET** `/tasks`  
   Retorna todas as tarefas do usuário logado. Admins podem visualizar todas as tarefas.

### 6. **Obter Tarefas por Status**
   **GET** `/tasks/status/{status}`  
   Filtra as tarefas por status (ex: "PENDENTE", "EM_ANDAMENTO", "CONCLUIDO").

---

## Como Usar o Swagger

O **Swagger** foi integrado à aplicação para facilitar o entendimento e teste da API. Para acessar a interface gráfica do Swagger:

1. Inicie a aplicação.
2. Acesse o Swagger no navegador:
   ```
   http://localhost:8080/swagger-ui/
   ```

No Swagger, você encontrará uma documentação interativa das APIs, onde poderá testar as requisições diretamente, visualizar os parâmetros esperados e consultar os exemplos de resposta.

---

## Autenticação e Autorização

A aplicação utiliza **JWT** para autenticação. O fluxo básico é:

1. O usuário realiza login com o endpoint **/login**.
2. Um token JWT é retornado.
3. O token é utilizado nas requisições subsequentes através do cabeçalho **Authorization**.

Exemplo de header para autenticação:
```http
Authorization: Bearer {token}
```

Os usuários podem ter um dos seguintes papéis:

- **ADMIN**: Acesso total a todas as funcionalidades.
- **BASIC**: Acesso limitado às tarefas próprias.

---

## Estrutura de Banco de Dados

A base de dados utiliza duas entidades principais:

1. **Users**: Representa os usuários da aplicação, que possuem um nome de usuário, senha e um conjunto de papéis (roles).
2. **Task**: Representa as tarefas, com atributos como título, descrição, status, prioridade e associação com um usuário.

---

## Contribuindo

Contribuições são bem-vindas! Se você deseja melhorar o projeto ou corrigir algum problema, siga as etapas abaixo:

1. Fork o repositório.
2. Crie uma nova branch para a sua funcionalidade (`git checkout -b minha-nova-funcionalidade`).
3. Faça as alterações desejadas e commite-as (`git commit -m 'Adicionando nova funcionalidade'`).
4. Push para a sua branch (`git push origin minha-nova-funcionalidade`).
5. Abra um pull request.

---

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
