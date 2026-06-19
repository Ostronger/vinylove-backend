import { Link, Outlet } from "react-router-dom";

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
                    <Link to="/staff">Dashboard</Link>
                    <Link to="/staff/scan">Scanner</Link>
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