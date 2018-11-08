import { Report } from './report';
import { Status } from './status';

export const REPORTS: Report[] = [
  {
    id: 0,
    titolo: 'Prova',
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.accettato
  },
  {
    id: 1,
    titolo: 'Prova',
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.rifiutato
  },
  {
    id: 2,
    titolo: 'Prova',
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.attesa
  }
]
