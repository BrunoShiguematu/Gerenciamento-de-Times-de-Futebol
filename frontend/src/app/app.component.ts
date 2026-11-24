import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: false,
  template: `
    <header class="marca">
      <a routerLink="/simulador" class="logo">⚽ Copa dos Quatro</a>
      <nav>
        <a routerLink="/simulador" routerLinkActive="ativo">Simulador</a>
        <a routerLink="/historico" routerLinkActive="ativo">Histórico</a>
        <a routerLink="/elenco" routerLinkActive="ativo">Elencos</a>
      </nav>
    </header>
    <router-outlet />
  `,
})
export class AppComponent {}
