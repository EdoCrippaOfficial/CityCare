import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin'

admin.initializeApp();

export const helloWorld = functions.https.onRequest((request, response) => {
    response.send("Hello Pazzo from Firebase Cloud Functions!");
});

export const getReportsID = functions.https.onRequest((request, response) => {
    admin.firestore().collection("reports").get()
    .then(snapshot => {
        const array = [];
        snapshot.forEach(doc => {
            array.push(doc.data().title)
        })
        response.send(array);
    })
    .catch(error => {
        response.status(500).send(error);
    })

})

export const unaStellaGratis = functions.firestore.document('reports/{reportId}')
    .onCreate((snap, context) => {
        
        // Get an object representing the document
        const newValue = snap.data();
  
        // access a particular field as you would any JS property
        const stars_count = newValue.n_stars;
  
        // Then return a promise of a set operation to update the stars count
        return snap.ref.set({
            star: stars_count + 1
          }, {merge: true});
    });