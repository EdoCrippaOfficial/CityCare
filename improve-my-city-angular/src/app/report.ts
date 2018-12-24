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
  user_id: string;
  image: Observable<string | null> //Not in firebase model

  constructor(report: Report) {
    this.description = report.description;
    this.id = report.id;
    this.n_stars = report.n_stars;
    this.operator_id = report.operator_id;
    this.position = report.position;
    this.reply = report.reply;
    this.status = report.status;
    this.timestamp = report.timestamp;
    this.title = report.title;
    this.user_id = report.user_id;
    this.image = report.image;
  }

  toUploadableObject() {
    let uploadableObject = Object.assign({}, this);
    delete uploadableObject.image;
    return uploadableObject;
  }
}
