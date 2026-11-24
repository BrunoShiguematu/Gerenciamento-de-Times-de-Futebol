import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { ElencoComponent } from './elenco.component';
import { HistoricoComponent } from './historico.component';
import { SimuladorComponent } from './simulador.component';

const rotas: Routes = [
  { path: '', redirectTo: 'simulador', pathMatch: 'full' },
  { path: 'simulador', component: SimuladorComponent },
  { path: 'historico', component: HistoricoComponent },
  { path: 'elenco', component: ElencoComponent },
];

@NgModule({
  declarations: [AppComponent, ElencoComponent, SimuladorComponent, HistoricoComponent],
  imports: [BrowserModule, FormsModule, RouterModule.forRoot(rotas)],
  providers: [provideHttpClient(withInterceptorsFromDi())],
  bootstrap: [AppComponent],
})
export class AppModule {}
