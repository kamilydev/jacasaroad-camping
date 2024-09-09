# jacasaroad-camping

Bem-vindo ao **JaCasa Road**, uma plataforma de anúncios de espaços de camping. Este projeto foi desenvolvido utilizando **Spring Boot** para o backend e **Thymeleaf** como motor de template para renderizar as páginas no frontend. O sistema permite que usuários anunciem espaços, gerenciem seus anúncios, e busquem por espaços disponíveis de acordo com diversos critérios.

## Funcionalidades

### Autenticação e Autorização

- Login e logout de usuários.
- Criação de novos usuários.
- Atualização de dados de usuários, incluindo imagem de perfil.

### Gerenciamento de Espaços

- Criação de novos anúncios de espaços para camping.
- Atualização de anúncios existentes, incluindo a possibilidade de adicionar, editar ou remover imagens associadas aos espaços.
- Exclusão de anúncios de espaços.
- Visualização e filtragem de todos os anúncios disponíveis.
- Listagem de anúncios por anfitrião.

### Busca e Filtragem

- Busca avançada por anúncios de espaços com múltiplos critérios como localização, preço, e facilidades disponíveis.

## Estrutura do Projeto

O projeto está estruturado da seguinte forma:

- **Controllers:** Controladores para gerenciar as rotas e redirecionar para as páginas correspondentes ou retornar dados JSON.
- **Models:** Representação das entidades do banco de dados.
- **Repositories:** Interfaces para persistência de dados utilizando Spring Data JPA.
- **Services:** Camada de serviço que contém a lógica de negócios.
- **Views:** Templates Thymeleaf para renderizar o frontend.

---

## Biblioteca de API - Importe e utilize no Postman

[[JACASAROAD].postman_collection.json](https://github.com/user-attachments/files/16936571/JACASAROAD.postman_collection.json)


