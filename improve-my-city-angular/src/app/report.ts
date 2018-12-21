//Class that represents reports
import { Observable } from 'rxjs';
import { Status } from './status';

export class Report {
  description: string;
  id: string;
  n_stars: number;
  operator_id: string;
  position: number;
  reply: string;
  status: Status;
  timestamp: Date;
  title: string;
  image: Observable<string | null>;
  user_id: string;
}
