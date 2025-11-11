import { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import Header from "./Header";
import { AdminContext } from "../contexts/Admin.context";
import "../cssComponents/LoginAdmin.css";
import Loading from "../loaders/LoadingCircle";

const LoginAdmin = () => {
    const [emailOrUsername, setEmailOrUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { admins, loading } = useContext(AdminContext);
    const navigate = useNavigate();

    // Configuración de Google OAuth
    // IMPORTANTE: redirect_uri sigue apuntando al backend
    const googleAuthBaseURL = "https://accounts.google.com/o/oauth2/v2/auth";
    const redirectURI = encodeURIComponent("http://localhost:8080/grant-code");
    const responseType = "code";
    const clientID = "963183553397-c5h3msr4v0kuhjp7rn9ab7ejqmbhvisg.apps.googleusercontent.com";
    const scope = encodeURIComponent("https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid");
    const accessType = "offline";

    const googleLoginURL = `${googleAuthBaseURL}?redirect_uri=${redirectURI}&response_type=${responseType}&client_id=${clientID}&scope=${scope}&access_type=${accessType}`;

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!emailOrUsername || !password) {
            setError("Por favor, complete todos los campos");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: emailOrUsername,
                    password: password
                }),
            });

            if (!response.ok) {
                throw new Error(response.status === 404 ? "Usuario no encontrado" : "Contraseña incorrecta");
            }

            const data = await response.json();
            console.log("Respuesta del backend:", data);

            // Mapea la respuesta del backend al formato que espera el frontend
            const adminData = {
                id: data.userId || data.id,
                firstName: data.firstName,
                lastName: data.lastName,
                email: data.email,
                role: data.role,
                accessToken: data.accessToken,
                refreshToken: data.refreshToken,
                expirationTime: data.expirationTime,
                authMethod: 'custom',
                // Si tu backend devuelve un campo 'name', úsalo; si no, combina firstName y lastName
                name: data.name || `${data.firstName} ${data.lastName}`
            };

            console.log("Datos guardados en sessionStorage:", adminData); // Para debugging

            sessionStorage.setItem("currentAdmin", JSON.stringify(adminData));
            navigate("/ShowAdmins");
        } catch (error) {
            setError(error.message || "Error al iniciar sesión. Por favor intenta nuevamente.");
        }
    };

    const handleGoogleLogin = () => {
        window.location.href = googleLoginURL;
    };

    if (loading) {
        return <Loading />;
    }

    return (
        <>
            <Header />
            <div className="containerFather">
                <Link className="ButtonAccept" to={"/"}>
                    Home
                </Link>
                <div className="container">
                    <h2 className="text-initial">Iniciar Sesión Como Admin</h2>
                    {error && <div className="error-message">{error}</div>}

                    {/* Botón de Google Login */}
                    <div className="google-login-section" style={{ marginBottom: '20px' }}>
                        <button
                            onClick={handleGoogleLogin}
                            className="google-login-btn"
                            type="button"
                            style={{
                                width: '100%',
                                backgroundColor: '#db4437',
                                color: 'white',
                                fontWeight: '600',
                                padding: '12px',
                                borderRadius: '8px',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                gap: '8px',
                                border: 'none',
                                cursor: 'pointer',
                                transition: 'background-color 0.3s'
                            }}
                            onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#c23321'}
                            onMouseOut={(e) => e.currentTarget.style.backgroundColor = '#db4437'}
                        >
                            <img
                                src="https://www.svgrepo.com/show/475656/google-color.svg"
                                alt="Google"
                                style={{ width: '24px', height: '24px' }}
                            />
                            Iniciar sesión con Google
                        </button>
                    </div>

                    {/* Divisor */}
                    <div style={{
                        display: 'flex',
                        alignItems: 'center',
                        margin: '20px 0',
                        gap: '10px'
                    }}>
                        <div style={{ flex: 1, height: '1px', backgroundColor: '#ccc' }}></div>
                        <span style={{ color: '#666', fontSize: '14px' }}>O continúa con</span>
                        <div style={{ flex: 1, height: '1px', backgroundColor: '#ccc' }}></div>
                    </div>

                    {/* Formulario tradicional */}
                    <form onSubmit={handleSubmit}>
                        <div className="information">
                            <label className="textInput" htmlFor="emailOrUsername">
                                Correo o Nombre de Usuario
                            </label>
                            <input
                                id="emailOrUsername"
                                type="text"
                                value={emailOrUsername}
                                onChange={(e) => setEmailOrUsername(e.target.value)}
                                placeholder="Ingrese su correo o nombre de usuario"
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