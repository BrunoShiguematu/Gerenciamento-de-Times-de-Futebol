import { Component, OnInit, inject, signal } from '@angular/core';

import { FutebolService } from './futebol.service';
import { Estatisticas, Jogador, JogadorRequest, Time } from './models';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private service = inject(FutebolService);

  posicoes = ['Goleiro', 'Zagueiro', 'Lateral', 'Volante', 'Meia', 'Atacante'];

  times = signal<Time[]>([]);
  timeAtual = signal<Time | null>(null);
  jogadores = signal<Jogador[]>([]);
  estatisticas = signal<Estatisticas | null>(null);
  mensagem = signal('');
  formAberto = signal(false);
  editandoId = signal<number | null>(null);
  form: JogadorRequest = this.formVazio(0);

  ngOnInit() {
    this.service.listarTimes().subscribe({
      next: (times) => {
        this.times.set(times);
        if (times.length > 0) {
          this.selecionar(times[0]);
        }
      },
      error: () => this.mensagem.set('Não foi possível carregar os times. O backend está ligado?'),
    });
  }

  selecionar(time: Time) {
    this.timeAtual.set(time);
    this.formAberto.set(false);
    this.carregar();
  }

  carregar() {
    const time = this.timeAtual();
    if (!time) {
      return;
    }
    this.service.listarJogadores(time.id).subscribe({
      next: (lista) => {
        this.jogadores.set(lista);
        this.mensagem.set('');
      },
      error: () => this.mensagem.set('Erro ao carregar os jogadores.'),
    });
    this.service.estatisticas(time.id).subscribe({
      next: (dados) => this.estatisticas.set(dados),
      error: () => this.mensagem.set('Erro ao carregar as estatísticas.'),
    });
  }

  novoJogador() {
    const time = this.timeAtual();
    if (!time) {
      return;
    }
    this.editandoId.set(null);
    this.form = this.formVazio(time.id);
    this.formAberto.set(true);
  }

  editar(jogador: Jogador) {
    this.editandoId.set(jogador.id);
    this.form = {
      nome: jogador.nome,
      posicao: jogador.posicao,
      idade: jogador.idade,
      gols: jogador.gols,
      numeroCamisa: jogador.numeroCamisa,
      timeId: jogador.time.id,
    };
    this.formAberto.set(true);
  }

  salvar() {
    const f = this.form;
    if (!f.nome.trim() || f.idade === null || f.gols === null || f.numeroCamisa === null) {
      this.mensagem.set('Preencha nome, idade, gols e número da camisa.');
      return;
    }
    const id = this.editandoId();
    const requisicao = id === null ? this.service.criar(f) : this.service.atualizar(id, f);
    requisicao.subscribe({
      next: () => {
        this.formAberto.set(false);
        this.mensagem.set('');
        this.carregar();
      },
      error: () => this.mensagem.set('Não foi possível salvar o jogador.'),
    });
  }

  excluir(jogador: Jogador) {
    if (!confirm(`Excluir ${jogador.nome}?`)) {
      return;
    }
    this.service.excluir(jogador.id).subscribe({
      next: () => this.carregar(),
      error: () => this.mensagem.set('Não foi possível excluir o jogador.'),
    });
  }

  cancelar() {
    this.formAberto.set(false);
  }

  private formVazio(timeId: number): JogadorRequest {
    return { nome: '', posicao: 'Meia', idade: null, gols: 0, numeroCamisa: null, timeId };
  }
}