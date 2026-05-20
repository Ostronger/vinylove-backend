export default function AdminDashboardPage() {
    const handleLogout = () => {
    localStorage.clear();
    window.location.href = "/";
};
    return (
        <div>
            <h1>Dashboard Admin</h1>
            <button onClick={handleLogout}>Déconnexion</button>
        </div>
    );
}