# ☁️ Integração com HubSpot - Desafio Técnico Meetime

Este projeto consiste em uma API REST desenvolvida com **Spring Boot**, com objetivo de integrar com a API do HubSpot utilizando o **OAuth 2.0 (authorization code flow)**, além de expor endpoints para criação de contatos e recebimento de webhooks.


---

## 📌 Visão Geral

A aplicação possui os seguintes endpoints:

1. **/oauth/authorize** – Gera a URL de autorização do OAuth2 com o HubSpot.
2. **/oauth/callback** – Recebe o `code` do HubSpot e troca por `access_token`.
3. **/contact/create** – Cria um novo contato no CRM da HubSpot.
4. **/webhook** – Recebe eventos do tipo `contact.creation` via webhook.

---

## ⚙️ Pré-requisitos

- Java 17
- Maven 3.8+
- Conta de desenvolvedor HubSpot (https://developers.hubspot.com/)
- Chave da aplicação registrada no HubSpot com redirect URI configurado

---

## ▶️ Como executar localmente

1. Clone o repositório:
   ```
   $ git clone https://github.com/Noltim/hubspot-Integration.git
   ```
2. No diretório da aplicação:
   ```
   $ cd hubspot-integration
   ```
3. Limpe e compile a aplicação:
   ```
   $ mvn clean install
   ```
4. Rode o projeto:
   ```
   $ mvn spring-boot:run
   ```

## 🔬 Como testar os endpoints

Para utiliza-los basta fazer o download e importá-la no seu Postman:
[Download da coleção Postman](https://github.com/RaquelAmbroziof/hubspot-integration/blob/main/HUBSPOT.postman_collection.json)

1. Obter URL de autorização

```
GET http://localhost:8080/oauth/authorize
```
ou user o curl a baixo:
```
curl --location 'http://localhost:8080/oauth/authorize'
```
➡️ Acesse a URL retornada no navegador, autorize a aplicação, e você será redirecionado para o callback com um code.

2. Crie uma nova conta ou faça login para autorizar.

3. Após autorizar, você será redirecionado a uma página onde o código de autorização será exibido. Copie o token de autorização.

```
Token recebido com sucesso: CMmelqvhMhIHAAEAQAAAARjFn9YXIISD4yUo-P6ABTIU40jJiSm6MyjnWbwLVbo6jUkZRps6MAAAAEEAAAAAAAAAAAAAAAAAgAAAAAAAAAAAACAAAAAAAOABAAAAAAAAAAAAAAAQAkIU6TWQUj3Ez-goILCw1tomSRb8lr1KA25hMVIAWgBgAGiEg-MlcAA
```
   
4. O HubSpot retornará o token de acesso. Copie o token em seguida, faça uma requisição POST para `http://localhost:8080/contact/create` para criar um novo contato. Passando a auth como Bearer token e passe os dados conforme o exemplo a baixo para obter sucesso na chamada.

```
curl --location 'http://localhost:8080/contact/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer CMmelqvhMhIHAAEAQAAAARjFn9YXIISD4yUo-P6ABTIU40jJiSm6MyjnWbwLVbo6jUkZRps6MAAAAEEAAAAAAAAAAAAAAAAAgAAAAAAAAAAAACAAAAAAAOABAAAAAAAAAAAAAAAQAkIU6TWQUj3Ez-goILCw1tomSRb8lr1KA25hMVIAWgBgAGiEg-MlcAA' \
--data-raw '{
    "properties:": {
        "firstName": "Roger",
        "lastName": "Vieira",
        "email": "rogervieira@gmail.com",
        "phone": "81997512774"
    }
}'
```

5. Webhook (testes com webhook.site)


```
Acesse https://webhook.site/#!/view/b43d6636-6d65-4c28-afba-d74958ac27d7/53f13060-6938-4e47-b7da-3ba4dc72751b/1

```

Click em XHR Redirect e preencha os seguintes dados:


```
Target: http://localhost:8080/webhook
Content-Type: application/json
HTTP Method: Post
```

O seguinte resultado aparecerá nos logs:

```
2025-04-08T09:30:13.320-03:00  INFO 4576 --- [hubspot-integration] [nio-8080-exec-3] c.h.h.controller.WebhookController       : 🔔 Webhook recebido: [{eventId=2649422408, subscriptionId=3420688, portalId=49641258, appId=10502008, occurredAt=1743953577163, subscriptionType=contact.creation, attemptNumber=0, objectId=111852009288, changeFlag=CREATED, changeSource=INTEGRATION, sourceId=10502008}]
```


## 📄 Documentação Técnica

✅ Decisões Tomadas

1. Framework: Spring Boot 3.4.4

     -  Escolhido por sua robustez, facilidade de integração com o ecossistema Spring (Spring Web, Security), e excelente suporte à construção de APIs REST.

2. Estrutura do Projeto

    -   Segregação em camadas (controller, service, dto, config) para aplicar o princípio da responsabilidade única (SRP – SOLID).

    -   Foco em manter baixo acoplamento e alta coesão entre componentes.

3. Armazenamento do Token

    - Para fins de simplicidade e foco no desafio, o token OAuth é armazenado em memória (TokenService). Em produção, seria persistido de forma segura (ex: banco com criptografia, Secrets Manager, Redis, etc).

4. Logs e Visibilidade

    - Logs foram adicionados nos principais pontos (ex: recebimento de webhook, criação de contatos, callback OAuth) usando SLF4J para facilitar o monitoramento e depuração.

5. Uso de DTOs

    - Padronizamos os dados de entrada com ContactRequestDTO para evitar o uso direto de entidades e permitir evolução flexível da API.

6. Boas práticas de segurança

    - Implementado CorsConfig para controle de acesso.

    - Criada SecurityConfig para deixar a aplicação preparada para autenticação futura, e com endpoints liberados conforme necessário.

7. Exposição de credenciais no application.properties
    - Durante a entrega deste desafio técnico, optei por manter as credenciais visíveis no application.properties. Essa decisão teve caráter demonstrativo e foi pensada para facilitar a execução e a análise da aplicação sem necessidade de configuração extra.

    - Por quê? Para garantir que o avaliador consiga testar a aplicação imediatamente com a integração funcionando.

    - Segurança? Esses dados não representam risco de segurança pois são limitados ao ambiente de testes da própria prova técnica e podem ser revogados a qualquer momento.

    - Em um ambiente de produção, o correto seria proteger esses dados com variáveis de ambiente ou ferramentas como Spring Cloud Config ou gerenciadores de segredos (AWS Secrets Manager, Vault etc).



## 📦 Bibliotecas Utilizadas

| Biblioteca                   | Justificativa                                                                 |
|------------------------------|------------------------------------------------------------------------------|
| `spring-boot-starter-web`    | Criação dos endpoints REST e estrutura da aplicação.                         |
| `spring-boot-starter-security` | Preparar a aplicação para controle de segurança e autenticação.             |
| `spring-boot-starter-logging` | Observabilidade da aplicação por meio de logs estruturados.                 |
| `spring-boot-devtools` *(dev)* | Facilitar o hot-reload durante desenvolvimento.                            |
| `lombok` *(opcional)*         | Reduzir codigo, mas não foi essencial neste caso.                      |
| `jackson` *(incluso no Web)* | Conversão automática entre JSON e objetos Java (serialize/deserialize).     |
| `RestTemplate` *(nativo Spring)* | Comunicação com a API do HubSpot (poderia ser substituído por WebClient). |


## 💡 Possíveis Melhorias Futuras

1. Persistência Segura do Token OAuth

    - Armazenar os tokens de forma segura em banco de dados ou em um serviço de secrets.

2. Renovação automática de token

    - Adicionar lógica para renovar o token de acesso usando o refresh token antes de sua expiração.

3. Validação de Webhook com HMAC

    - Implementar verificação da assinatura X-HubSpot-Signature para garantir a autenticidade do payload.

4. Tratamento avançado de erros

    - Criar uma camada de @ControllerAdvice com ExceptionHandler para padronizar respostas de erro.

5. Rate Limiting e Retry

    - Tratar status 429 retornado pela API da HubSpot com backoff exponencial e/ou retries com limites.

6. Testes automatizados

    - Criar testes unitários para os serviços principais e testes de integração para endpoints.

7. Documentação Swagger/OpenAPI

    - Gerar documentação automática da API para facilitar uso por terceiros.

