# Gerenciador de Tarefas

## Resumo do Projeto

O **Gerenciador de Tarefas** é uma aplicação backend desenvolvida em **Java** com **Spring Boot** para gerenciar tarefas. Ele oferece funcionalidades como:

- Criação, atualização, visualização e exclusão de tarefas.
- Controle de status e prioridade das tarefas.
- Sistema de autenticação e autorização usando **JWT** (JSON Web Token).
- Diferentes permissões de usuários (Admin e Usuário Básico).

A aplicação utiliza o banco de dados **MySQL** e implementa a arquitetura de **REST API**, com todas as operações sendo acessíveis via HTTP. O projeto foi configurado para ser executado utilizando **Docker**, facilitando a instalação e execução.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (Framework principal)
- **Spring Security** (Autenticação e autorização com JWT)
- **JPA/Hibernate** (Mapeamento objeto-relacional)
- **MySQL** (Banco de dados)
- **Docker** (Contêineres para rodar o ambiente)
- **Swagger** (Documentação da API)
- **Maven** (Gerenciamento de dependências)

## Como Usar o Projeto

### 1. Pré-requisitos

- **Java 17** ou superior (para rodar localmente sem Docker).
- **Docker** (para rodar o projeto com o banco de dados MySQL em contêineres).
- **Docker Compose** (para facilitar o gerenciamento dos contêineres).

### 2. Como Rodar a Aplicação

#### 2.1 Rodando com Docker

A aplicação foi configurada para ser executada dentro de contêineres utilizando **Docker**. Isso inclui a execução do banco de dados **MySQL** em um contêiner e a própria aplicação **Spring Boot** em outro contêiner.

1. Clone o repositório para o seu computador:
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-de-tarefas.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd gerenciador-de-tarefas
   ```

3. No arquivo `application.properties` (ou `application.yml`), configure a conexão com o banco de dados MySQL:

   ```properties
   spring.datasource.url=jdbc:mysql://mysql-container:3306/gerenciador_tarefas
   spring.datasource.username=root
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

   **Importante**: O nome do container MySQL (neste caso, `mysql-container`) deve coincidir com o nome definido no arquivo `docker-compose.yml` (veja mais detalhes abaixo).

4. Certifique-se de que o arquivo **`docker-compose.yml`** está no diretório raiz do seu projeto. Caso não tenha, crie-o com a seguinte configuração:

   ```yaml
   version: '3'
   services:
     mysql:
       image: mysql:8
       container_name: mysql-container
       environment:
         MYSQL_ROOT_PASSWORD: root
         MYSQL_DATABASE: gerenciador_tarefas
       ports:
         - "3306:3306"
       volumes:
         - mysql-data:/var/lib/mysql

     app:
       image: openjdk:17-jdk-slim
       container_name: gerenciador-tarefas-app
       build: .
       environment:
         SPRING_PROFILES_ACTIVE: docker
       ports:
         - "8080:8080"
       depends_on:
         - mysql
       volumes:
         - .:/app
       working_dir: /app
       command: ["./mvnw", "spring-boot:run"]

   volumes:
     mysql-data:
   ```

5. Execute o Docker Compose para construir e iniciar os contêineres:
   ```bash
   docker-compose up --build
   ```

6. A aplicação estará disponível em `http://localhost:8080` e o banco de dados MySQL será acessível na porta `3306`.

#### 2.2 Rodando Localmente (Sem Docker)

Caso você não queira usar o Docker, siga os passos abaixo para rodar a aplicação localmente:

1. Configure o banco de dados **MySQL** localmente e crie um banco chamado `gerenciador_tarefas`.
2. Altere as configurações do banco de dados no arquivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador_tarefas
   spring.datasource.username=root
   spring.datasource.password=sua_senha
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
3. Compile e execute a aplicação com o Maven:
   ```bash
   mvn clean spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

---

## Como Usar as Requisições

A API do Gerenciador de Tarefas permite as seguintes operações:

### 1. **Criar Usuário**
   
   **POST** `/users/create`  
   Cria um novo usuário no sistema.

   **Exemplo de request:**
   ```json
   {
       "username": "usuario1",
       "password": "senha123"
   }
   ```

### 2. **Login e Obter Token JWT**
   
   **POST** `/auth/login`  
   Realiza o login do usuário e retorna um **JWT** (token de autenticação).

   **Exemplo de request:**
   ```json
   {
       "username": "usuario1",
       "password": "senha123"
   }
   ```

   **Exemplo de resposta:**
   ```json
   {
       "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

   **Importante**: O **`access_token`** retornado deverá ser usado nas requisições subsequentes para autorizar o acesso às rotas protegidas.

   **Exemplo de cabeçalho com o token JWT**:
   ```http
   Authorization: Bearer {access_token}
   ```

### 3. **Criar Tarefa**
   **POST** `/tasks`  
   Cria uma nova tarefa associada ao usuário autenticado.

   **Exemplo de request:**
   ```json
   {
       "title": "Nova Tarefa",
       "description": "Descrição da tarefa",
       "status": "PENDENTE",
       "priority": "ALTA"
   }
   ```

### 4. **Atualizar Tarefa**
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

### 5. **Excluir Tarefa**
   **DELETE** `/tasks/{id}`  
   Exclui uma tarefa existente.

### 6. **Obter Tarefas**
   **GET** `/tasks`  
   Retorna todas as tarefas do usuário autenticado. Admins podem visualizar todas as tarefas.

### 7. **Obter Tarefas por Status**
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

## Autenticação e Autorização com JWT

A autenticação é realizada utilizando **JWT (JSON Web Token)**. O fluxo básico de autenticação funciona da seguinte maneira:

1. **Criação de Usuário**: O administrador ou usuário pode criar um novo usuário através do endpoint `/users/create`.
   
2. **Login e Obtenção do Token**: Após criar o usuário, ele pode fazer login com seu nome de usuário e senha no endpoint `/auth/login`. Se as credenciais estiverem corretas, será retornado um **JWT**.

3. **Acesso às Rotas Protegidas**: Para acessar as rotas protegidas, como criação de tarefas ou visualização de tarefas, o usuário precisa enviar o **JWT** no cabeçalho da requisição como um Bearer token.

   Exemplo de cabeçalho com o token JWT:
   ```http
   Authorization: Bearer {access_token}
   ```

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
