## TODOOOOO

### Generale

#### Iterazione 1
- [x] Inizializzazione Firebase Database e Storage

#### Iterazione 2
- [x] Inizializzazione Firebase auth
- [x] Scrittura cloud functions per la cancellazione di un report (consistenza tra database e storage di immagini)
---
### Android

#### Iterazione 1

- [x] Splittare VP per i fragment
- [x] Completare l'invio a DB
- [x] Implementare la ricezione dal DB
- [x] Swipe update
- [x] Implementare transazione per invio a DB (controllare che sia andato tutto bene cioè sia immagine che dati altrimenti rollback)
- [x] Sistemare i layout anche per landscape
- [x] Azzerare i campi di "New report" nel caso la transazione sia ok
- [x] Aggiungere intent fotocamera
- [x] Sort
- [x] Photoview (prova)
- [x] Implementare tutti i campi db
- [x] Ruotare correttamente le foto
- [ ] TESTING E VERIFICA DEL SOFTWARE (fix)

#### Iterazione 2

- [x] Pagina iniziale con log-in e sign-in
- [x] Inserimento di user_id e user_name nell’invio di nuove segnalazioni
- [x] Visualizzazione in fragment separati di segnalazioni attive e completate
- [x] Pagina per visualizzare solo le segnalazioni dell’utente corrente
- [x] Possibilità di cancellare una segnalazione fatta dall’utente corrente
- [ ] TESTING E VERIFICA DEL SOFTWARE

#### Iterazione 3

- [x] Sistema stelle
- [ ] Aggiornamento locale temporaneo all'aggiunta/rimozione di nuove stelle
- [ ] Posizione GPS per nuove segnalazioni (?)
- [ ] TESTING E VERIFICA DEL SOFTWARE
---
### Web

#### Iterazione 1

- [x] Struttura sito
- [x] Visualizzazione base report
- [x] Routing

#### Iterazione 2

- [x] Pagina iniziale con log-in
- [x] Visualizzazione segnalazioni secondo stato
- [x] Cambio status e commento operatore
- [x] Aggiornamento su firebase di status e commento operatore
- [x] Logica auth
- [x] Logica timeout auth

#### Iterazione 3
- [x] Abbellimento
- [x] Sistema a stelle
- [x] Ordinamento data & stelle
