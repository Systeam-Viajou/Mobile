# Mobile

Este repositório contém uma aplicação Java destinada à criação do aplicatio Viajou. Utilizando o Firebase e a api de Postgres E MongoDB.

## Estrutura do Projeto

- `Animacoes`: Nossa splashscreen com animação
- `TelasEntrada`: Tela de login e telas para realizar o cadastro
- `TelasErro`: Tela de erro de internet, internal error e erro de sms
- `TelasPrincipais`: Telas principais do nosso aplicativo, como a home, turismo, eventos e excursões
- `telasSecundarias`: Telas que você tem que passar pela tela principal para chegar nelas, tela de perfil, notificação, configuração, e formulario
- `TelasTour`: Telas que é aberta quando clicamos em algum ponto turistico, é nela que posui as informações adicionais sobre aquele ponto turistico e o tour virtual
- `TelaCardEventoAberto`: Assim como no TelasTour aqui tem a tela para quando a pessoa cliacr em um evento

## Funcionalidade

  1. **Conexão com Bancos de Dados**: Estabelece conexões com os bancos de dados PostgreSQL e MongoDB por meio da api, e o Firebase.
  2. **Autenticação**: Autentifica a pessoa criando o cadastro para ela poder fazer o login novamente a qualquer momento
  3. **Notificação**: Dispara uma notificação quando o usuário é cadastrado

## Configuração Inicial

1. Clone o repositório:
git clone https://github.com/Systeam-Viajou/Mobile.git

2. Rode o projeto no emulador do Android Studio ou em um celular android.

#### Desenvolvido com ❤ e carinho pela equipe de desenvolvimento *Viajou*:

- [Evellyn Nakamura](https://github.com/evellynNakamura)
- [João Oliveira](https://github.com/Jampeta)
