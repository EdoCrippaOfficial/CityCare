import { Observable } from 'rxjs';
import { Status } from './status';
import { Category } from './category';

/**
 * Classe che rappresenta i report
 */
export class Report {
  /**
   * Categoria del report
   */
  category: Category;
  /**
   * Descrizione del report
   */
  description: string;
  /**
   * ID del report
   */
  id: string;
  /**
   * Numero di stelle del report
   */
  n_stars: number;
  /**
   * Operatore che si è occupato del report
   */
  operator_id: string;
  /**
   * Risposta dell'operatore al report
   */
  reply: string;
  /**
   * Status del report
   */
  status: Status;
  /**
   * Data del report
   */
  timestamp: Date;
  /**
   * Titolo del report
   */
  title: string;
  /**
   * ID dell'utente che ha pubblicato il report
   */
  user_id: string;
  /**
   * Immagine del report
   */
  image: Observable<string | null> //Not in firebase model

  /**
   * Costruttore di un nuovo report da un report esistente
   */
  constructor(report: Report) {
    this.category = report.category;
    this.description = report.description;
    this.id = report.id;
    this.n_stars = report.n_stars;
    this.operator_id = report.operator_id;
    this.reply = report.reply;
    this.status = report.status;
    this.timestamp = report.timestamp;
    this.title = report.title;
    this.user_id = report.user_id;
    this.image = report.image;
  }

  /**
   * @param
   * @returns Report in formato adatto per l'upload
   */
  toUploadableObject() {
    let uploadableObject = Object.assign({}, this);
    delete uploadableObject.image;
    return uploadableObject;
  }
}
