import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Estatisticas, Jogador, JogadorRequest, Time } from './models';

@Injectable({ providedIn: 'root' })
export class FutebolService {
  private http = inject(HttpClient);
  private api = '/api';

  listarTimes(): Observable<Time[]> {
    return this.http.get<Time[]>(this.api + '/times');
  }

  listarJogadores(timeId: number): Observable<Jogador[]> {
    return this.http.get<Jogador[]>(this.api + '/times/' + timeId + '/jogadores');
  }

  estatisticas(timeId: number): Observable<Estatisticas> {
    return this.http.get<Estatisticas>(this.api + '/times/' + timeId + '/estatisticas');
  }

  criar(dados: JogadorRequest): Observable<Jogador> {
    return this.http.post<Jogador>(this.api + '/jogadores', dados);
  }

  atualizar(id: number, dados: JogadorRequest): Observable<Jogador> {
    return this.http.put<Jogador>(this.api + '/jogadores/' + id, dados);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(this.api + '/jogadores/' + id);
  }
}