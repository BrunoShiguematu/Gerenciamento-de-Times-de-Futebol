import { Component, OnInit, inject, signal } from '@angular/core';

import { CampeonatoService } from './campeonato.service';
import { Ranking, ResumoCampeonato } from './models';

@Component({
  selector: 'app-historico',
  standalone: false,
  templateUrl: './historico.component.html',
})
export class HistoricoComponent implements OnInit {
  private api = inject(CampeonatoService);

  lista = signal<ResumoCampeonato[]>([]);
  ranking = signal<Ranking | null>(null);
  editandoId = signal<number | null>(null);
  excluindoId = signal<number | null>(null);
  nomeEditado = '';
  mensagem = signal('');

  ngOnInit() {
    this.carregar();
  }

  carregar() {
    this.api.historico().subscribe({
      next: (l) => this.lista.set(l),
      error: () => this.mensagem.set('Não foi possível carregar o histórico. A API está ligada?'),
    });
    this.api.ranking().subscribe((r) => this.ranking.set(r));
  }

  editar(c: ResumoCampeonato) {
    this.editandoId.set(c.id);
    this.nomeEditado = c.nome;
  }

  salvarNome(c: ResumoCampeonato) {
    this.api.renomear(c.id, this.nomeEditado).subscribe({
      next: () => {
        this.editandoId.set(null);
        this.carregar();
      },
      error: () => this.mensagem.set('Informe um nome válido.'),
    });
  }

  excluir(c: ResumoCampeonato) {
    // 1º clique pede confirmação na própria linha; o 2º exclui
    if (this.excluindoId() !== c.id) {
      this.excluindoId.set(c.id);
      return;
    }
    this.excluindoId.set(null);
    this.api.excluir(c.id).subscribe(() => this.carregar());
  }

  maior(lista: { total: number }[]) {
    return Math.max(1, ...lista.map((x) => x.total));
  }
}
