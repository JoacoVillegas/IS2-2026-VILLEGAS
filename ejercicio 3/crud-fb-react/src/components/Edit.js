import React, {useState, useEffect} from 'react'
import { useNavigate, useParams } from 'react-router'
import { getDoc, updateDoc, doc } from 'firebase/firestore'
import { db } from '../firebaseConfig/firebase'

const Edit = () => {
  
  const [description, setDescription] = useState('')
  const [stock, setStock] = useState(0)
  
  const navigate = useNavigate()
  const {id} = useParams()

  const  update = async (e) => {
    e.preventDefault()
    const product = doc(db, "productos", id)
    const data = {descripcion: description, stock: stock}
    await updateDoc(product, data)
    navigate('/')
 }

  const getProductById = async (id) => {
       const product = await getDoc( doc(db, "productos", id) )
       if (product.exists()) {
        console.log(product.data())
        setDescription(product.data().descripcion)
        setStock(product.data().stock)
       } else {
        console.log('El producto no existe')
       }
    }

  useEffect( () => {
    getProductById(id)
  }, [])
    return (
    <div className='container' >
        <div className='row'>
            <div className='col'>
                <h1>editar producto</h1>
                <form onSubmit = {update}>
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
                        <button type='submit' className='btn btn-primary'>actualizar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
  )
}

export default Edit