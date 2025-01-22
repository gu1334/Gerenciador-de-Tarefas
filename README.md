
# Gerenciador de Tarefas

## Resumo do Projeto

O **Gerenciador de Tarefas** é uma aplicação backend desenvolvida em **Java** com **Spring Boot** para gerenciar tarefas. Ela oferece as seguintes funcionalidades principais:

- **Gestão de Tarefas:** Criação, atualização, visualização e exclusão de tarefas.
- **Controle de Status e Prioridade:** Organize tarefas por status e prioridade.
- **Autenticação e Autorização:** Protegido por **JWT** (JSON Web Token), com diferentes permissões de usuários (Admin e Usuário Básico).
- **Persistência de Dados:** Utiliza **MySQL** como banco de dados relacional.
- **Arquitetura RESTful** com endpoints acessíveis via HTTP.
- **Contêineres Docker** para facilitar a execução e deployment.

---

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal.
- **Spring Boot**: Framework para construção da aplicação.
- **Spring Security**: Gerenciamento de autenticação e autorização com **JWT**.
- **JPA/Hibernate**: Mapeamento objeto-relacional com o banco de dados.
- **MySQL**: Banco de dados relacional.
- **Docker**: Para containerização da aplicação e do banco de dados.
- **Swagger**: Documentação da API interativa.
- **Maven**: Gerenciamento de dependências e build da aplicação.

---

## Como Usar o Projeto

### 1. Pré-requisitos

- **Java 17** ou superior para rodar a aplicação localmente.
- **Docker** e **Docker Compose** para executar a aplicação em contêineres.

### 2. Rodando a Aplicação

#### 2.1 Usando Docker

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-de-tarefas.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd gerenciador-de-tarefas
   ```

3. Configure a conexão com o banco de dados MySQL no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://mysql-container:3306/gerenciador_tarefas
   spring.datasource.username=root
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. Certifique-se de ter o arquivo `docker-compose.yml` no diretório raiz. Caso não tenha, crie-o conforme abaixo:
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

5. Execute o Docker Compose:
   ```bash
   docker-compose up --build
   ```

6. A aplicação estará disponível em `http://localhost:8080` e o banco de dados MySQL será acessível na porta `3306`.

#### 2.2 Rodando Localmente

1. Configure o MySQL localmente e crie o banco `gerenciador_tarefas`.
2. Altere as configurações de banco de dados no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador_tarefas
   spring.datasource.username=root
   spring.datasource.password=sua_senha
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
3. Compile e execute a aplicação com Maven:
   ```bash
   mvn clean spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

---

## Endpoints da API

A API do Gerenciador de Tarefas oferece os seguintes endpoints:

### 1. **Criar Usuário**
   **POST** `/users/create`  
   Cria um novo usuário no sistema.

### 2. **Login e Obter Token JWT**
   **POST** `/auth/login`  
   Realiza login e retorna um **JWT** para autenticação.

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

   **Usar o JWT**:
   ```http
   Authorization: Bearer {access_token}
   ```

### 3. **Criar Tarefa**
   **POST** `/tasks`  
   Cria uma nova tarefa associada ao usuário autenticado.

### 4. **Atualizar Tarefa**
   **PUT** `/tasks/{id}`  
   Atualiza uma tarefa existente.

### 5. **Excluir Tarefa**
   **DELETE** `/tasks/{id}`  
   Exclui uma tarefa.

### 6. **Obter Tarefas**
   **GET** `/tasks`  
   Retorna todas as tarefas do usuário autenticado.

### 7. **Obter Tarefas por Status**
   **GET** `/tasks/status/{status}`  
   Filtra tarefas por status.

---

## Swagger

O **Swagger** foi integrado para facilitar o uso da API. Acesse a interface interativa:

1. Inicie a aplicação.
2. Acesse o Swagger:
   ```
   http://localhost:8080/swagger-ui/
   ```

---

## Autenticação e Autorização

A autenticação é feita com **JWT**. O fluxo é o seguinte:

1. **Criar Usuário**: Endpoint `/users/create`.
2. **Login e Token**: Endpoint `/auth/login` retorna um JWT.
3. **Acessar Endpoints Protegidos**: Inclua o JWT no cabeçalho das requisições subsequentes.

---

## Contribuindo

1. Faça o fork do repositório.
2. Crie uma nova branch (`git checkout -b minha-nova-funcionalidade`).
3. Faça suas alterações e commite-as.
4. Envie um pull request.

---

## Licença

Este projeto está licenciado sob a MIT License. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
