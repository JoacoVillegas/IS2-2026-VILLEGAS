import React, {useState} from 'react'
import { useNavigate } from 'react-router'
import {  collection, addDoc } from 'firebase/firestore'
import { db } from '../firebaseConfig/firebase'

const Create = () => {
 const [description, setDescription] = useState('')
 const [ stock, setStock] = useState(0);
 const navigate = useNavigate()
 
 const productsCollection = collection(db, "productos")

 const store = async (e) => {
    e.preventDefault()
    await addDoc(productsCollection, {descripcion: description, stock: stock})
    navigate('/')
 }

  return (
    <div className='container' >
        <div className='row'>
            <div className='col'>
                <h1>crear producto</h1>
                <form onSubmit = {store}>
                    <div className='mb-3'>
                        <label className='form-label'>descripcion</label>
                        <input value={description} onChange={ (e) => setDescription(e.target.value)}
                        type='text'
                        className='form-control'>
                        
                        </input>
                    </div>

                    <div className='mb-3'>
                        <label className='form-label'>stock </label>
                        <input value={stock} onChange= { (e) => setStock(e.target.value)}
                        type='number' className='form-control'>
                        </input>
                        <button type='submit' className='btn btn-primary'>Store</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
  )
}

export default Create