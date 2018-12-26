import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

// Firebase app initialization
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

});

export const updateStars = functions.firestore.document('reports/{reportId}')
    .onUpdate((change, context) => {

        // Get an object representing the document
        const report = change.after.data();

        // access a particular field as you would any JS property
        const stars_list = report.users_starred;

        // Then return a promise of a set operation to update the stars count
        return change.after.ref.set({
            n_stars: stars_list.length
        }, {merge: true});
    });

export const deleteImages = functions.firestore.document('reports/{reportId}').onDelete((snap, context) => {
    const record = snap.data();
    const id = record.id;
    const path = "/images/" + id;
    const bucket = admin.storage().bucket('improvemycity-ab42e.appspot.com');
    const file = bucket.file(path + "_img");
    return file.delete();
});

export const deleteThumb = functions.firestore.document('reports/{reportId}').onDelete((snap, context) => {
    const record = snap.data();
    const id = record.id;
    const path = "/images/" + id;
    const bucket = admin.storage().bucket('improvemycity-ab42e.appspot.com');
    const file = bucket.file(path + "_thumb");
    return file.delete();
});
