export interface Time {
  id: number;
  nome: string;
  cidade: string;
  anoFundacao: number;
  estadio: string;
}

export interface Jogador {
  id: number;
  nome: string;
  posicao: string;
  idade: number;
  gols: number;
  numeroCamisa: number;
  time: Time;
}

export interface JogadorRequest {
  nome: string;
  posicao: string;
  idade: number | null;
  gols: number | null;
  numeroCamisa: number | null;
  timeId: number;
}

export interface Estatisticas {
  totalJogadores: number;
  idadeMedia: number;
  totalGols: number;
  artilheiro: string | null;
}
// ---------- Simulador de campeonato ----------

export interface Campeonato {
  id: number;
  nome: string;
  criadoEm: string;
  status: 'EM_ANDAMENTO' | 'FINALIZADO';
  campeao: Time | null;
}

export interface Partida {
  id: number;
  campeonatoId: number;
  fase: 'GRUPO' | 'SEMI' | 'FINAL';
  ordem: number;
  mandante: Time;
  visitante: Time;
  golsMandante: number | null;
  golsVisitante: number | null;
  penaltisMandante: number | null;
  penaltisVisitante: number | null;
}

export interface Gol {
  id: number;
  partidaId: number;
  jogador: Jogador;
  minuto: number;
}

export interface LinhaClassificacao {
  timeId: number;
  time: string;
  jogos: number;
  vitorias: number;
  empates: number;
  derrotas: number;
  golsPro: number;
  golsContra: number;
  saldo: number;
  pontos: number;
}

export interface CampeonatoDetalhe {
  campeonato: Campeonato;
  partidas: Partida[];
  gols: Gol[];
  classificacao: LinhaClassificacao[];
}

export interface PlacarRequest {
  golsMandante: number | null;
  golsVisitante: number | null;
  penaltisMandante: number | null;
  penaltisVisitante: number | null;
}

export interface ResumoCampeonato {
  id: number;
  nome: string;
  criadoEm: string;
  status: string;
  campeao: string | null;
  totalGols: number;
  placarFinal: string | null;
}

export interface Contagem {
  nome: string;
  total: number;
}

export interface Ranking {
  titulos: Contagem[];
  artilharia: Contagem[];
}
