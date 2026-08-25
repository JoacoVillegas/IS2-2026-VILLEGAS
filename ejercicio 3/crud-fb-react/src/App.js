import './App.css';
import Create from './components/Create';
import Edit from './components/Edit';
import Show from './components/Show';


import { BrowserRouter, Route, Routes } from 'react-router';

function App() {
  return (
    <div className="App">
      <h1>Hola firebase</h1>
      <button className='btn btn-primary'>crear</button>
      
      <BrowserRouter>
        <Routes>
          <Route path='/' element= { <Show/> } ></Route>
          <Route path='/create' element= { <Create/> } ></Route>
          <Route path='/edit/:id' element= { <Edit/> } ></Route>
        </Routes>
      </BrowserRouter>

    </div>
  );
}

export default App;
