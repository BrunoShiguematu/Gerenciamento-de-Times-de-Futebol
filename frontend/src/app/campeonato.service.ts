import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Campeonato, CampeonatoDetalhe, PlacarRequest, Ranking, ResumoCampeonato } from './models';

@Injectable({ providedIn: 'root' })
export class CampeonatoService {
  private http = inject(HttpClient);
  private api = '/api/campeonatos';

  historico(): Observable<ResumoCampeonato[]> {
    return this.http.get<ResumoCampeonato[]>(this.api);
  }

  ranking(): Observable<Ranking> {
    return this.http.get<Ranking>(this.api + '/ranking');
  }

  criar(nome: string | null): Observable<CampeonatoDetalhe> {
    return this.http.post<CampeonatoDetalhe>(this.api, { nome });
  }

  detalhe(id: number): Observable<CampeonatoDetalhe> {
    return this.http.get<CampeonatoDetalhe>(this.api + '/' + id);
  }

  renomear(id: number, nome: string): Observable<Campeonato> {
    return this.http.put<Campeonato>(this.api + '/' + id, { nome });
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(this.api + '/' + id);
  }

  simularPartida(partidaId: number): Observable<CampeonatoDetalhe> {
    return this.http.post<CampeonatoDetalhe>(this.api + '/partidas/' + partidaId + '/simular', {});
  }

  salvarPlacar(partidaId: number, placar: PlacarRequest): Observable<CampeonatoDetalhe> {
    return this.http.put<CampeonatoDetalhe>(this.api + '/partidas/' + partidaId, placar);
  }
}
