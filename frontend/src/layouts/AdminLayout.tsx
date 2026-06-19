import { NavLink,Outlet } from "react-router-dom";
import "./AdminLayout.css";

export default function AdminLayout() {
    return (
        <div className="admin-layout">
            <aside className="admin-sidebar">
                <h2>VinyLove Admin</h2>

                <nav>
                    <NavLink to="/admin" end>Dashboard</NavLink>
                    <NavLink to="/admin/events">Événements</NavLink>
                    <NavLink to="/admin/scan">Scan</NavLink>
                    <NavLink to="/admin/staff">Staff</NavLink>
                </nav>
                
                <button onClick={() => {
                    localStorage.clear();
                    window.location.href = "/";
                }}>
                    Déconnexion
                </button>
            </aside>

            <main className="admin-main">
                <Outlet />
            </main>
        </div>
    );
}