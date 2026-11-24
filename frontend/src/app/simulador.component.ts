import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { CampeonatoService } from './campeonato.service';
import { CampeonatoDetalhe, Partida, PlacarRequest } from './models';

const espera = (ms: number) => new Promise((ok) => setTimeout(ok, ms));
const sorteia = (max: number) => Math.floor(Math.random() * max);

@Component({
  selector: 'app-simulador',
  standalone: false,
  templateUrl: './simulador.component.html',
})
export class SimuladorComponent implements OnInit {
  private api = inject(CampeonatoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  detalhe = signal<CampeonatoDetalhe | null>(null);
  rolando = signal<Record<number, { m: number; v: number }>>({});
  recem = signal<number | null>(null);
  ocupado = signal(false);
  mensagem = signal('');
  mostrarCampeao = signal(false);
  edicao: Record<number, PlacarRequest> = {};

  grupo = computed(() => this.fase('GRUPO'));
  semis = computed(() => this.fase('SEMI'));
  final = computed(() => this.fase('FINAL')[0] ?? null);
  campeao = computed(() => this.detalhe()?.campeonato.campeao ?? null);
  finalizado = computed(() => this.detalhe()?.campeonato.status === 'FINALIZADO');

  ngOnInit() {
    this.route.queryParamMap.subscribe((p) => {
      const id = Number(p.get('id'));
      if (!id) {
        this.detalhe.set(null);
      } else if (this.detalhe()?.campeonato.id !== id) {
        this.api.detalhe(id).subscribe({
          next: (d) => this.aplicar(d),
          error: () => this.mensagem.set('Campeonato não encontrado. A API está ligada?'),
        });
      }
    });
  }

  async iniciar(simularTudo: boolean) {
    try {
      const d = await firstValueFrom(this.api.criar(null));
      this.aplicar(d);
      this.mostrarCampeao.set(false);
      await this.router.navigate(['/simulador'], { queryParams: { id: d.campeonato.id } });
      if (simularTudo) {
        await espera(400);
        await this.simularTudo();
      }
    } catch {
      this.mensagem.set('Não foi possível criar o campeonato. A API está ligada?');
    }
  }

  async simularUm(p: Partida) {
    if (this.ocupado()) return;
    this.ocupado.set(true);
    try {
      await this.animar(p);
    } catch {
      this.mensagem.set('Erro ao simular a partida.');
    }
    this.ocupado.set(false);
    this.checarCampeao();
  }

  // Simula jogo por jogo, com animação. fase = 'GRUPO', 'SEMI', 'FINAL' ou null (tudo)
  async simularTudo(fase: string | null = null) {
    if (this.ocupado()) return;
    this.ocupado.set(true);
    this.mensagem.set('');
    try {
      let proxima = this.proximaPendente(fase);
      while (proxima) {
        await this.animar(proxima);
        proxima = this.proximaPendente(fase);
      }
    } catch {
      this.mensagem.set('Erro ao simular. A API está ligada?');
    }
    this.ocupado.set(false);
    this.checarCampeao();
  }

  salvar(p: Partida) {
    this.api.salvarPlacar(p.id, this.edicao[p.id]).subscribe({
      next: (d) => {
        this.mensagem.set('');
        this.recem.set(p.id);
        this.aplicar(d);
        this.checarCampeao();
      },
      error: () => this.mensagem.set('Placar inválido. Empate no mata-mata precisa de pênaltis diferentes.'),
    });
  }

  private async animar(p: Partida) {
    document.getElementById('p' + p.id)?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    this.recem.set(null);
    const giro = setInterval(() => {
      this.rolando.update((r) => ({ ...r, [p.id]: { m: sorteia(6), v: sorteia(6) } }));
    }, 70);
    try {
      const [d] = await Promise.all([firstValueFrom(this.api.simularPartida(p.id)), espera(1100)]);
      this.aplicar(d);
    } finally {
      clearInterval(giro);
      this.rolando.update((r) => {
        const copia = { ...r };
        delete copia[p.id];
        return copia;
      });
    }
    this.recem.set(p.id);
    await espera(p.fase === 'GRUPO' ? 550 : 900);
  }

  private checarCampeao() {
    if (this.finalizado()) {
      setTimeout(() => this.mostrarCampeao.set(true), 300);
    }
  }

  private aplicar(d: CampeonatoDetalhe) {
    this.detalhe.set(d);
    this.edicao = {};
    for (const p of d.partidas) {
      this.edicao[p.id] = {
        golsMandante: p.golsMandante,
        golsVisitante: p.golsVisitante,
        penaltisMandante: p.penaltisMandante,
        penaltisVisitante: p.penaltisVisitante,
      };
    }
  }

  private proximaPendente(fase: string | null) {
    return this.detalhe()?.partidas.find((p) => p.golsMandante === null && (!fase || p.fase === fase)) ?? null;
  }

  // Ainda tem jogo sem placar nesta fase? (habilita o botão)
  pendente(fase: string) {
    return this.proximaPendente(fase) !== null;
  }

  private fase(nome: string) {
    return (this.detalhe()?.partidas ?? []).filter((p) => p.fase === nome).sort((a, b) => a.ordem - b.ordem);
  }

  semi(ordem: number) {
    return this.semis().find((p) => p.ordem === ordem) ?? null;
  }

  empateMataMata(p: Partida) {
    const e = this.edicao[p.id];
    return p.fase !== 'GRUPO' && e?.golsMandante != null && e.golsMandante === e.golsVisitante;
  }

  alterado(p: Partida) {
    const e = this.edicao[p.id];
    if (!e || e.golsMandante == null || e.golsVisitante == null) return false;
    return (
      e.golsMandante !== p.golsMandante ||
      e.golsVisitante !== p.golsVisitante ||
      (this.empateMataMata(p) &&
        (e.penaltisMandante !== p.penaltisMandante || e.penaltisVisitante !== p.penaltisVisitante))
    );
  }

  venceu(p: Partida, timeId: number) {
    if (p.golsMandante === null || p.golsVisitante === null) return false;
    let m = p.golsMandante - p.golsVisitante;
    if (m === 0 && p.penaltisMandante !== null && p.penaltisVisitante !== null) {
      m = p.penaltisMandante - p.penaltisVisitante;
    }
    return m === 0 ? false : (m > 0 ? p.mandante.id : p.visitante.id) === timeId;
  }

  autores(p: Partida, timeId: number) {
    return (this.detalhe()?.gols ?? [])
      .filter((g) => g.partidaId === p.id && g.jogador.time.id === timeId)
      .sort((a, b) => a.minuto - b.minuto)
      .map((g) => g.jogador.nome + " " + g.minuto + "'")
      .join(' · ');
  }
}
