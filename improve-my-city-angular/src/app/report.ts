//Class that represents reports
import { Status } from './status';

export class Report {
  id: number;
  titolo: string;
  data: Date;
  descrizione: string;
  foto: File;
  utente: string;
  status: Status;
}
