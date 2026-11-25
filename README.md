# Copa Simulator: Gerenciamento de Times de Futebol

Sistema web que começou como um gerenciador de elencos de futebol e virou um **simulador de campeonato**. Ele usa os times e jogadores cadastrados no banco para sortear os resultados: quem tem o elenco mais goleador tem mais chance de vencer, e os gols são marcados pelos jogadores de verdade de cada time.

Trabalho final da disciplina de banco de dados.

- **Davi:** Banco de Dados (MySQL)
- **Diego:** API (Java com Spring Boot)
- **Bruno:** FrontEnd (Angular)

## O que o sistema faz

O sistema tem três telas.

**Simulador**
- Cria um campeonato com nome. O nome é obrigatório.
- Fase de grupos em pontos corridos, como no Brasileirão: todos jogam contra todos. Com os 5 times cadastrados são 10 jogos.
- A classificação é atualizada a cada jogo. Os 4 primeiros (em verde) vão para a semifinal e os demais (em vermelho) são eliminados. O desempate é por pontos, depois saldo de gols e depois gols marcados.
- Semifinal com 1º × 4º e 2º × 3º, e depois a final. Empate no mata-mata é decidido nos pênaltis.
- Cada placar pode ser digitado à mão ou sorteado. Também dá para simular tudo de uma vez, ou só a fase de grupos, só as semifinais ou só a final.
- Se um resultado da fase de grupos for alterado depois, a semifinal e a final são refeitas.

**Histórico**
- Lista todos os campeonatos salvos, com campeão, total de gols e placar da final.
- Permite abrir, renomear e excluir cada um.
- Mostra o ranking de títulos por time e os 5 maiores artilheiros de todos os campeonatos.

**Elencos**
- Cadastro, edição e exclusão de jogadores.
- Estatísticas de cada time: quantidade de jogadores, idade média, total de gols e artilheiro.

### Como o sorteio funciona

- **Força de cada time:** é a soma dos gols de todos os jogadores do elenco.
- **Gols do jogo:** são sorteados por uma distribuição de Poisson. A média de gols de cada time cresce com a força dele, e o mandante tem uma pequena vantagem.
- **Autor de cada gol:** é sorteado entre os jogadores do time. Quem tem mais gols na carreira tem mais chance, e goleiro não marca.
- **Pênaltis:** quando há empate no mata-mata, os pênaltis são sorteados e nunca terminam empatados.

## Requisitos do trabalho

| Requisito | Onde está |
|---|---|
| Tabelas relacionadas com PK e FK | `times`, `jogadores`, `campeonatos`, `partidas` e `gols` (`database/01-schema.sql` e `database/03-campeonato.sql`) |
| CRUD completo | Campeonatos (criar, ver, renomear e excluir), partidas (registrar e alterar placar) e jogadores |
| JOIN, agregação e subconsulta | Classificação (SUM com CASE), histórico, títulos (COUNT), artilharia (JOIN de 3 tabelas) e campeonatos acima da média de gols (subconsulta). Estão todas em `database/consultas/consultas.sql`. |
| Integração entre front e back | A tela Angular chama a API REST, e a API lê e grava no MySQL |

## Estrutura do repositório

```
.
├── docker-compose.yml        # sobe o MySQL já com tabelas e dados
├── database/
│   ├── 01-schema.sql         # banco e tabelas times e jogadores
│   ├── 02-dados.sql          # 4 times e 24 jogadores
│   ├── 03-campeonato.sql     # tabelas campeonatos, partidas e gols
│   ├── 04-flamengo.sql       # 5º time (Flamengo) e 6 jogadores
│   └── consultas/
│       └── consultas.sql     # consultas pedidas no trabalho
├── backend/                  # API REST em Java 21 + Spring Boot
└── frontend/                 # tela em Angular 20
```

## Como rodar no seu computador

### O que precisa estar instalado

- **Git**
- **Java 21** ou mais novo. Para conferir, rode `java -version`.
- **Node.js 20.19** ou mais novo. Para conferir, rode `node -v`.
- Um banco MySQL, que pode vir de duas formas:
  - **Docker Desktop** (opção A, a mais fácil), ou
  - **MySQL 8** instalado direto no computador (opção B).

Não precisa instalar o Maven: o projeto já vem com o `mvnw`, que baixa tudo sozinho.

### 1. Baixar o projeto

```bash
git clone https://github.com/BrunoShiguematu/Gerenciamento-de-Times-de-Futebol.git
```

```bash
cd Gerenciamento-de-Times-de-Futebol
```

### 2. Ligar o banco

**Opção A: com Docker.** Na raiz do projeto, rode:

```bash
docker compose up -d
```

Espere uns 30 segundos. Na primeira vez, o MySQL cria sozinho o banco `gerenciador_times`, todas as tabelas e os dados de exemplo a partir dos arquivos da pasta `database`. O usuário é `root` e a senha é `root`, que já é o que a API espera.

**Opção B: com MySQL instalado no computador.** Rode os 4 scripts em ordem, na raiz do projeto. O `-p` pede a senha do seu MySQL:

```bash
mysql -uroot -p < database/01-schema.sql
```

```bash
mysql -uroot -p < database/02-dados.sql
```

```bash
mysql -uroot -p < database/03-campeonato.sql
```

```bash
mysql -uroot -p < database/04-flamengo.sql
```

A API espera o usuário `root` com a senha `root`. Se a senha do seu MySQL for outra, informe a senha certa no passo 3.

### 3. Ligar a API (terminal 1)

```bash
cd backend
```

No Mac ou Linux:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

**Se a senha do MySQL não for `root`** (só na opção B), informe a senha antes de ligar a API. No Mac ou Linux, use um único comando:

```bash
SPRING_DATASOURCE_PASSWORD=sua_senha ./mvnw spring-boot:run
```

No Windows (PowerShell), rode estas duas linhas:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="sua_senha"
.\mvnw.cmd spring-boot:run
```

Se o MySQL não tiver senha, deixe o valor vazio: `SPRING_DATASOURCE_PASSWORD= ./mvnw spring-boot:run`.

Na primeira vez, o download das bibliotecas demora alguns minutos. A API está pronta quando aparecer `Started GerenciadorApplication`. Ela fica na porta 8080. Para testar, rode o comando abaixo em outro terminal. Ele deve devolver os 5 times.

```bash
curl http://localhost:8080/api/times
```

### 4. Ligar a tela (terminal 2)

Abra um novo terminal na raiz do projeto e rode:

```bash
cd frontend
```

```bash
npm install
```

```bash
npm start
```

O `npm install` só é necessário na primeira vez. Depois, acesse **http://localhost:4200**.

### 5. Testar

1. Na tela **Simulador**, clique em **Iniciar**, digite um nome e clique em **Iniciar e simular tudo**.
2. Os 10 jogos são sorteados um por um, a classificação se reorganiza, o 5º fica em vermelho e, depois da semifinal e da final, aparece o campeão.
3. Na tela **Histórico**, o campeonato aparece salvo, junto com o ranking de títulos e de artilheiros.

## Como desligar

- Nos terminais da API e da tela, aperte `Ctrl+C`.
- Opção A: rode `docker compose down` para desligar o banco mantendo os dados.
- Para apagar tudo e recriar o banco do zero, rode `docker compose down -v` e depois `docker compose up -d`.

## Endereços da API

| Método | Endereço | O que faz |
|---|---|---|
| GET | `/api/times` | Lista os times |
| GET | `/api/campeonatos` | Histórico de campeonatos |
| POST | `/api/campeonatos` | Cria um campeonato (corpo: `{"nome": "..."}`) |
| GET | `/api/campeonatos/{id}` | Partidas, gols e classificação de um campeonato |
| PUT | `/api/campeonatos/{id}` | Renomeia um campeonato |
| DELETE | `/api/campeonatos/{id}` | Exclui um campeonato e todos os seus jogos |
| POST | `/api/campeonatos/partidas/{id}/simular` | Sorteia o placar de uma partida |
| PUT | `/api/campeonatos/partidas/{id}` | Registra o placar digitado |
| GET | `/api/campeonatos/ranking` | Títulos por time e artilharia |

## Problemas comuns

| Sintoma | O que fazer |
|---|---|
| A tela diz que não conseguiu carregar os dados | A API ou o banco estão desligados. Confira os passos 2 e 3. |
| `Access denied for user 'root'` | A senha do MySQL não é `root`. Veja como informar a senha no passo 3. |
| `Communications link failure` ou `Connection refused` | O MySQL não está rodando. Ligue o MySQL (opção A ou B) e tente de novo. |
| `Unknown database` ou `Table ... doesn't exist` | Os scripts SQL não rodaram. Na opção A, rode `docker compose down -v` e depois `docker compose up -d`. Na opção B, rode os 4 scripts de novo. |
| `Port 8080 was already in use` | Já existe uma API rodando em outro terminal. Pare com `Ctrl+C`. |
| `./mvnw: Permission denied` | Rode `chmod +x mvnw` dentro da pasta `backend`. |
| O comando `java` não é reconhecido, ou dá erro de versão | Instale o Java 21 ou mais novo e abra um novo terminal. |
