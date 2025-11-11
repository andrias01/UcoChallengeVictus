import { createContext, useEffect, useState } from "react";

// Datos locales para testing
const mockAdmins = [
  {
    id: "7803ce6c-6ade-4db1-a194-5eaff3fb58ca",
    name: "Andres Felipe",
    lastName: "Velez Alcaraz",
    idType: "ID",
    idNumber: "1017245139",
    contactNumber: "3057477830",
    email: "parkourandres97@gmail.com",
    password: "sideghparkour19"
  },
  {
    id: "7803ce6c-6ade-4db1-a194-5eaff3fb58cb",
    name: "Juan Daniel",
    lastName: "Rodriguez Giraldo",
    idType: "ID",
    idNumber: "1007109578",
    contactNumber: "3127068048",
    email: "123@gmail.com",
    password: "1"
  },
  {
    id: "9083f135-cff5-4dce-bb75-0643b5616a20",
    name: "Benito",
    lastName: "Camelas bien",
    idType: "Cc",
    idNumber: "1134444",
    contactNumber: "305668438",
    email: "todoslosdias@gmail.com",
    password: "1233394949"
  },
  {
    id: "f946a5ec-fee4-4913-a694-a0f22bb0a35a",
    name: "Carlos David",
    lastName: "Velez Alcaraz",
    idType: "CC",
    idNumber: "101724659",
    contactNumber: "3057477830",
    email: "andres.velez5136@uco.net.co",
    password: "mariconPowoer"
  },
  {
    id: "4cee3daf-f997-483f-b71a-d0f0a752740c",
    name: "Juan Daniel",
    lastName: "Rodriguez Giraldo",
    idType: "CC",
    idNumber: "546846",
    contactNumber: "3127068080",
    email: "juan.rodriguez9584@uco.net.co",
    password: "pepitoPerez"
  },
  {
    id: "8cbd91f8-bbda-4146-875e-c2f977b3b82e",
    name: "Juan Pablo",
    lastName: "Avendaño Duque",
    idType: "CC",
    idNumber: "10003400",
    contactNumber: "305 3719053",
    email: "juanpower1@gmail.com",
    password: "power1"
  },
  {
    id: "7f05b9c7-e709-4ec1-8c17-c0885812bd08",
    name: "Luis Miguel",
    lastName: "Avendaño Duque",
    idType: "CC",
    idNumber: "5969493",
    contactNumber: "305 389000",
    email: "Miguel@gmail.com",
    password: "power2"
  }
];

const AdminContext = createContext();

function AdminProviderWrapper(props) {
  const [admins, setAdmins] = useState([]);
  const [filteredAdmins, setFilteredAdmins] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  // Simula la obtención de datos pero usa los datos locales
  const getAdmin = () => {
    try {
      // Simulamos un pequeño retraso para imitar una petición a API
        setTimeout(() => {
          setAdmins(mockAdmins);
          setFilteredAdmins(mockAdmins);
          setError(null);
          setLoading(false);
        }, 500);
    } catch (err) {
      console.error("Error al cargar administradores:", err);
      setError("No se pudo cargar la lista de administradores. Intenta más tarde.");
      setLoading(false);
    }
  };

  useEffect(() => {
    getAdmin();
  }, []);

  // Buscar por nombre
  const filterAdmins = (name) => {
    const filtered = admins.filter(admin =>
      admin.name.toLowerCase().includes(name.toLowerCase())
    );
    setFilteredAdmins(filtered);
  };

  const findAdminsByName = (name) => {
    // Simula una búsqueda pero usando datos locales
    return new Promise((resolve) => {
      setTimeout(() => {
        const filtered = mockAdmins.filter((admin) =>
          admin.name.toLowerCase().includes(name.toLowerCase())
        );
        resolve(filtered);
      }, 300);
    });
  };

  // Crear nuevo administrador
  const createAdmin = (newAdmin) => {
      return new Promise((resolve) => {
          setTimeout(() => {
            // Generamos un ID único
            const id = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
            const adminWithId = { ...newAdmin, id };
            
            // Actualizamos el estado con el nuevo administrador
            setAdmins(prevAdmins => [...prevAdmins, adminWithId]);
            setFilteredAdmins(prevFiltered => [...prevFiltered, adminWithId]);
            
            // También actualizamos nuestros datos mock para mantener consistencia
            mockAdmins.push(adminWithId);
            
            resolve({ success: true, data: adminWithId });
          }, 300);
      });
  };

  // Eliminar un administrador por ID
  const deleteAdmin = (id) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        // Filtramos el administrador del estado
        const updatedAdmins = admins.filter(admin => admin.id !== id);
        setAdmins(updatedAdmins);
        setFilteredAdmins(updatedAdmins);
        
        // También actualizamos nuestros datos mock
        const indexToRemove = mockAdmins.findIndex(admin => admin.id === id);
        if (indexToRemove !== -1) {
          mockAdmins.splice(indexToRemove, 1);
        }
        
        resolve({ success: true });
      }, 300);
    });
  };

  // Modificar un administrador por ID
  const updateAdmin = (id, updatedData) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        // Actualizamos en el estado
        const updatedAdmins = admins.map(admin => 
          admin.id === id ? { ...admin, ...updatedData } : admin
        );
        setAdmins(updatedAdmins);
        setFilteredAdmins(updatedAdmins);
        
        // También actualizamos nuestros datos mock
        const adminIndex = mockAdmins.findIndex(admin => admin.id === id);
        if (adminIndex !== -1) {
          mockAdmins[adminIndex] = { ...mockAdmins[adminIndex], ...updatedData };
        }
        
        resolve({ success: true });
      }, 300);
    });
  };

  return (
    <AdminContext.Provider value={{
      admins,
      filteredAdmins,
      setAdmins,
      getAdmin,
      filterAdmins,
      createAdmin,
      deleteAdmin,
      updateAdmin,
      findAdminsByName,
      error,
      loading,
    }}>
      {props.children}
    </AdminContext.Provider>
  );
}

export { AdminContext, AdminProviderWrapper };