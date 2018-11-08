import { Status } from './status';

export class Report {
  id: number;
  titolo: string;
  descrizione: string;
  foto: File;
  utente: string;
  status: Status;
}
