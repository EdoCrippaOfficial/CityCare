// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

/**
 * Costante che rappresenta l'environment Angular di test e contiene la configurazione per la connessione al database
 */
export const environment = {
  production: false,
  firebase: {
    apiKey: "AIzaSyCDjhZxwNVpdXLqGn1yMRCXlyJw_u5oJpY",
    authDomain: "improvemycity-ab42e.firebaseapp.com",
    databaseURL: "https://improvemycity-ab42e.firebaseio.com",
    projectId: "improvemycity-ab42e",
    storageBucket: "improvemycity-ab42e.appspot.com",
    messagingSenderId: "1048633976197"
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
