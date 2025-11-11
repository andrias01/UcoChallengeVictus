import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Dashboard from "./Dashboard";

function ShowAdmins() {
    const navigate = useNavigate();
    const [currentAdmin, setCurrentAdmin] = useState(null);

    useEffect(() => {
        const adminData = sessionStorage.getItem("currentAdmin");

        if (!adminData) {
            navigate("/LoginAdminPage");
        } else {
            const parsedAdmin = JSON.parse(adminData);
            setCurrentAdmin(parsedAdmin);

            if (parsedAdmin.expirationTime) {
                const expirationDate = new Date(parsedAdmin.expirationTime);
                const now = new Date();

                if (now > expirationDate) {
                    handleLogout();
                }
            }
        }
    }, [navigate]);

    const handleLogout = async () => {
        const adminData = sessionStorage.getItem("currentAdmin");

        if (adminData) {
            const admin = JSON.parse(adminData);
            try {
                await fetch("http://localhost:8080/api/auth/logout", {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${admin.accessToken}`,
                        "Content-Type": "application/json",
                    },
                });
            } catch (error) {
                console.error("Error al cerrar sesión en el backend:", error);
            }
        }

        sessionStorage.removeItem("currentAdmin");
        navigate("/LoginAdminPage");
    };

    if (!currentAdmin) {
        return null;
    }

    return (
        <div>
            {/* Header con información del usuario */}
            <div
                style={{
                    padding: "15px 30px",
                    backgroundColor: "#f8f9fa",
                    borderBottom: "1px solid #dee2e6",
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                }}
            >
                <div style={{ display: "flex", alignItems: "center", gap: "15px" }}>
                    {/* Imagen o inicial */}
                    {currentAdmin.profileImage ? (
                        <img
                            src={currentAdmin.profileImage}
                            alt="Foto de perfil"
                            style={{
                                width: "45px",
                                height: "45px",
                                borderRadius: "50%",
                                objectFit: "cover",
                                border: "2px solid #4285f4",
                            }}
                        />
                    ) : (
                        <div
                            style={{
                                width: "45px",
                                height: "45px",
                                borderRadius: "50%",
                                backgroundColor: "#4285f4",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                color: "white",
                                fontWeight: "bold",
                                fontSize: "18px",
                            }}
                        >
                            {currentAdmin.firstName
                                ? currentAdmin.firstName.charAt(0).toUpperCase()
                                : "U"}
                        </div>
                    )}

                    {/* Info del usuario */}
                    <div>
                        <div style={{ fontWeight: "600", fontSize: "16px" }}>
                            {currentAdmin.name ||
                                `${currentAdmin.firstName} ${currentAdmin.lastName}`}
                        </div>
                        <div style={{ fontSize: "14px", color: "#6c757d" }}>
                            {currentAdmin.email}
                        </div>
                        {currentAdmin.role && (
                            <div
                                style={{
                                    fontSize: "12px",
                                    color: "#28a745",
                                    fontWeight: "500",
                                }}
                            >
                                {currentAdmin.role}
                            </div>
                        )}
                    </div>
                </div>

                <button
                    onClick={handleLogout}
                    style={{
                        padding: "8px 20px",
                        backgroundColor: "#dc3545",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontWeight: "600",
                        transition: "background-color 0.3s",
                    }}
                    onMouseOver={(e) =>
                        (e.currentTarget.style.backgroundColor = "#c82333")
                    }
                    onMouseOut={(e) =>
                        (e.currentTarget.style.backgroundColor = "#dc3545")
                    }
                >
                    Cerrar Sesión
                </button>
            </div>

            <Dashboard />
        </div>
    );
}

export default ShowAdmins;