# Gerenciador de Tarefas

## Resumo do Projeto

O **Gerenciador de Tarefas** é uma aplicação backend desenvolvida em **Java** com **Spring Boot** para gerenciar tarefas. Ela oferece as seguintes funcionalidades principais:

- **Gestão de Tarefas:** Criação, atualização, visualização e exclusão de tarefas.
- **Controle de Status e Prioridade:** Organize tarefas por status e prioridade.
- **Autenticação e Autorização:** Protegido por **Spring Security** com **JWT**, incluindo diferentes permissões de usuários (Admin e Usuário Básico).
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
       build:
         context: .
       container_name: gerenciador-tarefas-app
       environment:
         SPRING_PROFILES_ACTIVE: docker
       ports:
         - "8080:8080"
       depends_on:
         - mysql

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

---

### 1. **Criar Usuário**
   **POST** `/users`  
   Cria um novo usuário no sistema.

   **Headers**:  
   - Content-Type: application/json  

   **Exemplo de Request:**
   ```json
   {
       "username": "usuario1",
       "password": "senha123"
   }
   ```

   **Exemplo de Response:**
   ```json
   {
       "message": "Usuário: usuario1 criado com sucesso"
   }
   ```

---

### 2. **Login e Obter Token JWT**
   **POST** `/auth/login`  
   Realiza login e retorna um **JWT** para autenticação.

   **Headers**:  
   - Content-Type: application/json  

   **Exemplo de Request:**
   ```json
   {
       "username": "usuario1",
       "password": "senha123"
   }
   ```

   **Exemplo de Response:**
   ```json
   {
       "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   ```

   **Usar o JWT** em requisições autenticadas:
   ```http
   Authorization: Bearer {access_token}
   ```

---

### 3. **Listar Usuários (Apenas para Admin)**
   **GET** `/users`  
   Lista todos os usuários cadastrados.

   **Headers**:  
   - Authorization: Bearer {access_token}  

   **Exemplo de Response:**
   ```json
   [
       {
           "id": 1,
           "username": "usuario1",
           "roles": ["BASIC"]
       },
       {
           "id": 2,
           "username": "admin",
           "roles": ["ADMIN"]
       }
   ]
   ```

---

### 4. **Criar Tarefa**
   **POST** `/tasks`  
   Cria uma nova tarefa associada ao usuário autenticado.

   **Headers**:  
   - Authorization: Bearer {access_token}  
   - Content-Type: application/json  

   **Exemplo de Request:**
   ```json
   {
       "title": "Comprar suprimentos",
       "description": "Ir ao supermercado para comprar suprimentos",
       "status": "TODO",
       "priority": "HIGH"
   }
   ```

   **Exemplo de Response:**
   ```json
   {
       "id": 1,
       "title": "Comprar suprimentos",
       "description": "Ir ao supermercado para comprar suprimentos",
       "status": "TODO",
       "priority": "HIGH",
       "created_at": "2025-01-01T12:00:00"
   }
   ```

---

### 5. **Obter Todas as Tarefas**
   **GET** `/tasks`  
   Retorna todas as tarefas do usuário autenticado.

   **Headers**:  
   - Authorization: Bearer {access_token}  

   **Exemplo de Response:**
   ```json
   [
       {
           "id": 1,
           "title": "Comprar suprimentos",
           "description": "Ir ao supermercado para comprar suprimentos",
           "status": "TODO",
           "priority": "HIGH"
       }
   ]
   ```

---

### 6. **Buscar Tarefas por Status**
   **GET** `/tasks/search`  
   Retorna todas as tarefas do usuário autenticado que possuem o status informado.

   **Parâmetro de Query:**  
   - `status`: Status da tarefa (exemplo: `TODO`, `IN_PROGRESS`, `DONE`).

   **Exemplo de Request:**  
   `GET /tasks/search?status=TODO`

   **Exemplo de Response:**  
   ```json
   [
       {
           "id": 1,
           "title": "Comprar suprimentos",
           "description": "Ir ao supermercado para comprar suprimentos",
           "status": "TODO",
           "priority": "HIGH"
       }
   ]
   ```

---

### 7. **Atualizar Tarefa**
   **PUT** `/tasks/{id}`  
   Atualiza uma tarefa existente pelo `id`.

   **Headers**:  
   - Authorization: Bearer {access_token}  
   - Content-Type: application/json  

   **Exemplo de Request:**
   ```json
   {
       "title": "Comprar alimentos",
       "description": "Ir ao supermercado para comprar alimentos frescos",
       "status": "IN_PROGRESS",
       "priority": "MEDIUM"
   }
   ```

   **Exemplo de Response:**
   ```json
   {
       "id": 1,
       "title": "Comprar alimentos",
       "description": "Ir ao supermercado para comprar alimentos frescos",
       "status": "IN_PROGRESS",
       "priority": "MEDIUM",
       "updated_at": "2025-01-01T15:00:00"
   }
   ```

---

### 8. **Excluir Tarefa**
   **DELETE** `/tasks/{id}`  
   Exclui uma tarefa existente pelo `id`.

   **Headers**:  
   - Authorization: Bearer {access_token}  

   **Exemplo de Response:**  
   ```json
   "Tarefa excluída com sucesso!"
   ```

---

