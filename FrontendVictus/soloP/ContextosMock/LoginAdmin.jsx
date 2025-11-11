import { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import Header from "./Header";
import { AdminContext } from "../contexts/Admin.context"; // Asegúrate que la ruta sea correcta
import '../cssComponents/LoginAdmin.css';
  
const LoginAdmin = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { admins } = useContext(AdminContext);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        // Validar que los campos no estén vacíos
        if (!email || !password) {
            setError("Por favor, complete todos los campos");
            return;
        }

        // Buscar el administrador en la lista por email y password
        const admin = admins.find(
            (admin) => admin.email === email && admin.password === password
        );

        if (admin) {
            // Guardar la información del administrador en sessionStorage
            sessionStorage.setItem("currentAdmin", JSON.stringify(admin));
            // Redirigir al dashboard
            navigate("/ShowAdmins");
        } else {
            setError("Credenciales incorrectas. Este administrador no existe.");
        }
    };

    return (
        <>
            <Header></Header>
            <div className="containerFather">
                <Link className="ButtonAccept" to={"/"}>
                    Home
                </Link>
                <div className="container">
                    <h2 className="text-initial">
                        Iniciar Sesión Como Admin
                    </h2>
                    {error && <div className="error-message">{error}</div>}
                    <form onSubmit={handleSubmit}>
                        <div className="information">
                            <label className="textInput" htmlFor="email">
                                Correo
                            </label>
                            <input 
                                id="email" 
                                type="text" 
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="information">
                            <label className="textInput" htmlFor="password">
                                Contraseña
                            </label>
                            <input 
                                id="password" 
                                type="password" 
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        <div className="linksWithButton">
                            <button className="ButtonAccept" type="submit">
                                Aceptar
                            </button>
                            <a className="linkForgetPassword" href="#">
                                ¿Olvidó la contraseña?
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default LoginAdmin;