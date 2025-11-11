import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Loading from "../loaders/LoadingCircle";

const GrantCode = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [error, setError] = useState("");

    useEffect(() => {
        try {
            console.log('=== Grant Code - Extrayendo parámetros ===');
            
            // Extraer y DECODIFICAR los parámetros de la URL
            const userId = searchParams.get('userId');
            const firstName = decodeURIComponent(searchParams.get('firstName') || '');
            const lastName = decodeURIComponent(searchParams.get('lastName') || '');
            const email = decodeURIComponent(searchParams.get('email') || '');
            const role = decodeURIComponent(searchParams.get('role') || 'USER');
            const accessToken = searchParams.get('accessToken');
            const refreshToken = searchParams.get('refreshToken');
            const expirationTime = searchParams.get('expirationTime');

            console.log('Parámetros recibidos:', {
                userId,
                firstName,
                lastName,
                email,
                role,
                accessToken: accessToken ? '***' + accessToken.slice(-8) : null,
                refreshToken: refreshToken ? '***' + refreshToken.slice(-8) : null,
                expirationTime
            });

            // Verificar que tengamos los datos mínimos necesarios
            if (!accessToken || !email) {
                console.error('❌ Faltan datos críticos');
                setError("No se recibieron los datos de autenticación");
                setTimeout(() => navigate("/LoginAdminPage"), 3000);
                return;
            }

            // Construir el objeto de usuario
            const adminData = {
                id: userId || null,
                firstName: firstName,
                lastName: lastName,
                email: email,
                role: role,
                accessToken: accessToken,
                refreshToken: refreshToken,
                expirationTime: expirationTime,
                authMethod: 'google'
            };

            console.log('✅ Datos procesados:', adminData);
            
            // Guardar en sessionStorage
            sessionStorage.setItem("currentAdmin", JSON.stringify(adminData));
            console.log('✅ Guardado en sessionStorage');
            
            // Redirigir al dashboard
            console.log('🔄 Redirigiendo a /ShowAdmins');
            navigate("/ShowAdmins");

        } catch (error) {
            console.error('❌ Error durante la autenticación:', error);
            setError("Error al autenticar con Google. Por favor intenta nuevamente.");
            setTimeout(() => navigate("/LoginAdminPage"), 3000);
        }
    }, [searchParams, navigate]);

    if (error) {
        return (
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                minHeight: '100vh',
                gap: '20px',
                padding: '20px'
            }}>
                <div style={{
                    color: 'red',
                    fontSize: '18px',
                    textAlign: 'center'
                }}>
                    {error}
                </div>
                <div>Redirigiendo al login...</div>
                <button
                    onClick={() => navigate("/LoginAdminPage")}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4285f4',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer'
                    }}
                >
                    Ir al login ahora
                </button>
            </div>
        );
    }

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            minHeight: '100vh',
            gap: '20px'
        }}>
            <Loading />
            <div>Autenticando con Google...</div>
            <div style={{ fontSize: '12px', color: '#666' }}>
                Por favor espera...
            </div>
        </div>
    );
};

export default GrantCode;