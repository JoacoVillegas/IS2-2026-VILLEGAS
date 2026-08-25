
import { initializeApp } from "firebase/app";
import { getFirestore } from '@firebase/firestore'
const firebaseConfig = {
  apiKey: "AIzaSyBVPayS7rapm989JavKio2xpvNAN58Sffw",
  authDomain: "crud-fire-react-a6a02.firebaseapp.com",
  projectId: "crud-fire-react-a6a02",
  storageBucket: "crud-fire-react-a6a02.firebasestorage.app",
  messagingSenderId: "288496261973",
  appId: "1:288496261973:web:085bd0f85a3194cc612ddc"
};


const app = initializeApp(firebaseConfig);

export const db = getFirestore(app);