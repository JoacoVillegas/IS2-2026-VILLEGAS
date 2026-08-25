import React, {useState, useEffect} from 'react'

import { Link } from 'react-router'
import { collection, getDocs, getDoc, deleteDoc, doc} from 'firebase/firestore'
import { db } from '../firebaseConfig/firebase'

import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'
const MySwal = withReactContent(Swal);

const Show = () => {

    //1- configuramos los hooks
    const [products, setProducts] = useState([])
    //2- referenciamos a la DB firestore (¿sera el nombre en el que tengo en Firestore?)
    const productsCollection = collection(db, "productos")
    //3- funcion para mostrar todos los docs
    const getProducts = async () => {
        const data = await getDocs(productsCollection)

        const productos = data.docs.map((doc) => ({
            ...doc.data(),
            id: doc.id
        }))

        console.log(productos)

        setProducts(productos)
    }


    useEffect(() => {
        getProducts()
    }, [])
    //4- funcion para eliminar un doc

    const deleteProduct = async (id) => {
        const productDoc = doc(db, "productos", id)
        await deleteDoc(productDoc)
        getProducts()
    }
    //5- funcion de confirmacion para sweet alert 2
    
    const confirmDelete = (id) => {
        Swal.fire({
            title: "Quiere eliminar el producto?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!"
            }).then((result) => {
            if (result.isConfirmed){
                console.log(result.isConfirmed)
                deleteProduct(id)
                Swal.fire({
                title: "Deleted!",
                text: "Your file has been deleted.",
                icon: "success"
            })};
            });
    }

    //6- usamos useEffect
    //7- devolvemos vista de nuestro componente
    return (
        <>
        <div className='container'>
            <div className='row'>
                <div className='col'>
                    <div className="d-grid gap-2">
                        <Link to="/create" className='btn btn-secondary mt-2 mb-2'>Create</Link>
                    </div>

                    <table className= 'table table-dark table-hover'>
                        <thead>
                            <tr>
                                <th>Descripcion</th>
                                <th>Stockn</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            { products.map ( (product) => (
                                <tr key= {product.id}>
                                    <td>{product.descripcion}</td>
                                    <td>{product.stock}</td>
                                    <td>
                                        <Link to={`/edit/${product.id}`} className= "btn btn-light"><i class="fa-regular fa-pen-to-square"></i></Link>
                                        <button onClick = { () => {confirmDelete(product.id)} } className='btn btn-danger'><i class="fa-solid fa-trash"></i></button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>

                    </table>
                </div>
            </div> 
        </div>
        </>
    )
  return (
    <div>Show</div>
  )
}

export default Show