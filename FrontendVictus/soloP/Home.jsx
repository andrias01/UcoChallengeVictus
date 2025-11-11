
import '../cssComponents/Home.css'
const Home = () => {
    
  return (
    <>
    <div class="header">
        <i class="fas fa-bars menu-icon"></i>
        <h1>Victus Residencias</h1>
        <i class="fas fa-question-circle help-icon"></i>
    </div>
    <div class="container">
        <a href="#" class="button">
            <i class="fas fa-cog"></i>
            Administrador
        </a>
        <a href="#" class="button">
            <i class="fas fa-user-shield"></i>
            Portería
        </a>
        <a href="#" class="button">
            <i class="fas fa-home"></i>
            Residente
        </a>
    </div>
    </>
  )
}

export default Home
