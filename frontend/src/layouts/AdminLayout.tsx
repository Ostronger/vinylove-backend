import { Link,Outlet } from "react-router-dom";
import "./AdminLayout.css";

export default function AdminLayout() {
    return (
        <div className="admin-layout">
            <aside className="admin-sidebar">
                <h2>VinyLove Admin</h2>

                <nav>
                    <Link to="/admin">Dashboard</Link>
                    <Link to="/admin/events">Événements</Link>
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