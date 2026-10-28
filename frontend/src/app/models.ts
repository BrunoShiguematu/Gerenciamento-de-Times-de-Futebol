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