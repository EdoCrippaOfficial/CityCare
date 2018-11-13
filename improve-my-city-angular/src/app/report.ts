//Class that represents reports
import { Observable } from 'rxjs';
import { Status } from './status';

export class Report {
  id: string;
  title: string;
  timestamp: Date;
  description: string;
  reply: string;
  image: Observable<string | null>;
  user: string;
  status: Status;
}
