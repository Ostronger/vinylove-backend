export default function StaffDashboardPage() {
    const handleLogout = () => {
    localStorage.clear();
    window.location.href = "/";
};
    return (
        <div>
            <h1>Dashboard Staff</h1>
            <button onClick={handleLogout}>Déconnexion</button>
        </div>
    );
}