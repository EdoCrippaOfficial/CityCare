//Class that represents reports
import { Observable } from 'rxjs';
import { Status } from './status';

export class Report {
  id: number;
  title: string;
  timestamp: Date;
  description: string;
  reply: string;
  image: Observable<any>;
  user: string;
  status: Status;
}
