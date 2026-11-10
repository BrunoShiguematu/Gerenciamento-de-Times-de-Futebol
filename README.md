# Gerenciamento de Times de Futebol

Sistema web para gerenciar o elenco de quatro times de futebol: São Paulo, Corinthians, Santos e Vasco. Permite consultar, cadastrar, editar e excluir jogadores, e mostra estatísticas de cada time (quantidade de jogadores, idade média, total de gols e artilheiro).

Trabalho da disciplina de banco de dados.

## Estrutura do repositório

```
.
├── docker-compose.yml     # sobe o MySQL já com as tabelas e os dados
├── database/              # scripts SQL
│   ├── 01-schema.sql      # cria o banco e as tabelas times e jogadores
│   ├── 02-dados.sql       # 4 times e 24 jogadores de exemplo
│   └── consultas/
│       └── consultas.sql  # consultas com JOIN, COUNT/AVG/SUM e subconsultas
├── backend/               # API REST em Java
└── frontend/              # tela em Angular
```

## Como rodar o projeto

Funciona no GitHub Codespaces ou em um computador com Docker, Java 21 ou superior e Node.js 20.19 ou superior. Os comandos abaixo são sempre rodados na **raiz do repositório**, a menos que diga o contrário. Use **três terminais**: um para o banco, um para a API e um para a tela.

### 1. Ligar o banco (terminal 1)

```bash
docker compose up -d
```

Espere uns 30 segundos e confira que o estado é `running`:

```bash
docker compose ps
```

Na primeira vez, o MySQL cria sozinho o banco `gerenciador_times`, as tabelas e os dados de exemplo, a partir dos arquivos da pasta `database`.

### 2. Ligar a API (terminal 2)

```bash
cd backend
```

```bash
./mvnw spring-boot:run
```

Na primeira vez, o Maven baixa as bibliotecas e demora alguns minutos. A API está pronta quando aparecer `Started GerenciadorApplication` no terminal. Ela responde na porta 8080. Para testar:

```bash
curl http://localhost:8080/api/times
```

Deve devolver os 4 times em JSON.

### 3. Ligar a tela (terminal 3)

```bash
cd frontend
```

Na primeira vez, instale as dependências:

```bash
npm install
```

Depois:

```bash
npm start
```

A tela roda na porta 4200 e repassa os pedidos `/api` para a API na porta 8080 (configuração em `frontend/proxy.conf.json`).

- No **Codespaces**: abra a aba **Portas**, procure a porta **4200** e clique no ícone de globo.
- No **computador local**: acesse http://localhost:4200.

### Resultado esperado

Quatro botões com os times, quatro cartões de números e a tabela do elenco. No São Paulo: 6 jogadores, idade média 33,5, 32 gols e artilheiro Calleri.

## Como desligar

Nos terminais da API e da tela, aperte `Ctrl+C`. Depois, para desligar o banco mantendo os dados:

```bash
docker compose down
```

Para desligar e apagar os dados (o banco é recriado do zero na próxima vez):

```bash
docker compose down -v
```

## Problemas comuns

| Sintoma | O que fazer |
|---|---|
| A tela mostra "Não foi possível carregar os times" | A API ou o banco estão desligados. Confira os terminais 1 e 2. |
| A API dá `Communications link failure` ou `Connection refused` | O banco não está rodando. Rode `docker compose up -d` e espere 30 segundos. |
| `Unknown database` ou tabelas inexistentes | O volume do banco é antigo. Rode `docker compose down -v` e depois `docker compose up -d`. |
| `Port 8080 was already in use` | Já há uma API rodando em outro terminal. Pare com `Ctrl+C`. |
| `./mvnw: Permission denied` | Rode `chmod +x mvnw` dentro da pasta `backend`. |
| Abrir a porta 8080 no navegador mostra "Whitelabel Error Page" | É normal: a API só tem endereços que começam com `/api`. |
