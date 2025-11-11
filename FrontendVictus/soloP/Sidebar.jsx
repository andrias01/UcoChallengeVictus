import "../cssComponents/Home.css";

const Sidebar = ({ isOpen, toggleSidebar }) => {
  return (
    <div className={`sidebar ${isOpen ? "open" : ""}`}>
      <button className="close-btn" onClick={toggleSidebar}>
        &times;
      </button>
      <nav>
        <ul>
          <li><a href="#">Nosotros</a></li>
          <li><a href="#">Crear Sesión</a></li>
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;