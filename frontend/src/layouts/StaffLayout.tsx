import { NavLink, Outlet } from "react-router-dom";
import "./StaffLayout.css";

export default function StaffLayout() {
    const handleLogout = () => {
        localStorage.clear();
        window.location.href = "/";
    };

    return (
        <div className="staff-layout">
            <aside className="staff-sidebar">
                <h2>Viny Love</h2>

                <nav>
                    <NavLink to="/staff" end>
                        Dashboard
                    </NavLink>
                    <NavLink to="/staff/scan">
                        Scanner
                    </NavLink>
                </nav>

                <button onClick={handleLogout}>
                    Déconnexion
                </button>
            </aside>

            <main>
                <Outlet />
            </main>
        </div>
    );
}