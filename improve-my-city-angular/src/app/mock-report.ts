//Mock reports for testing purposes
import { Report } from './report';
import { Status } from './status';

export const REPORTS: Report[] = [
  {
    id: 0,
    titolo: 'Prova',
    data: new Date("10/10/2018"),
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.accettato
  },
  {
    id: 1,
    titolo: 'Prova',
    data: new Date("10/11/2018"),
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.rifiutato
  },
  {
    id: 2,
    titolo: 'Prova',
    data: new Date("10/12/2018"),
    descrizione: 'prova',
    foto: null,
    utente: 'user',
    status: Status.attesa
  }
]
